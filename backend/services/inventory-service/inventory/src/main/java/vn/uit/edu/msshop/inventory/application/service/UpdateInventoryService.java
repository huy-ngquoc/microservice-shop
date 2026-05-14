package vn.uit.edu.msshop.inventory.application.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;
import vn.uit.edu.msshop.inventory.adapter.out.event.documents.InventoryUpdatedDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.InventoryUpdatedDocumentRepository;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderCancelledCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderDetailCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderShippedCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.UpdateInventoryCommand;
import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;
import vn.uit.edu.msshop.inventory.application.exception.InventoryNotFoundException;
import vn.uit.edu.msshop.inventory.application.mapper.InventoryViewMapper;
import vn.uit.edu.msshop.inventory.application.port.in.UpdateInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.out.LoadInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.PublishInventoryEventPort;
import vn.uit.edu.msshop.inventory.application.port.out.SaveInventoryPort;
import vn.uit.edu.msshop.inventory.bootstrap.config.cache.CacheNames;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.ReservedQuantity;

@Service
@RequiredArgsConstructor
public class UpdateInventoryService implements UpdateInventoryUseCase {
    private final SaveInventoryPort savePort;
    private final LoadInventoryPort loadPort;

    private final InventoryViewMapper mapper;
    

    

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.INVENTORY,
                            allEntries = true),
                    @CacheEvict(
                            cacheNames = CacheNames.INVENTORY_BY_VARIANT,
                            key = "#command.variantId().value()"),
                    @CacheEvict(
                            cacheNames = CacheNames.INVENTORY_LIST,
                            allEntries = true)
            })
    public InventoryView update(
            UpdateInventoryCommand command) {
        Inventory inventory = loadPort.loadByVariantId(command.variantId())
                .orElseThrow(() -> new InventoryNotFoundException(command.variantId()));
        final var u = Inventory.UpdateInfo.builder().inventoryId(inventory.getId())
                .quantity(command.quantity().apply(inventory.getQuantity()))
                .reservedQuantity(command.reservedQuantity().apply(inventory.getReservedQuantity()))
                .status(command.newStatus().apply(inventory.getStatus()))
                .build();
        final var next = inventory.applyUpdateInfo(u);
        final var saved = savePort.save(next);
        // publishEventPort.publishInventoryUpdateEvent(new
        // InventoryUpdated(saved.getVariantId().value(), saved.getQuantity().value(),
        // saved.getReservedQuantity().value()));
        
        return mapper.toView(saved);
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.INVENTORY,
                            allEntries = true),
                    @CacheEvict(
                            cacheNames = CacheNames.INVENTORY_BY_VARIANT,
                            allEntries = true),
                    @CacheEvict(
                            cacheNames = CacheNames.INVENTORY_LIST,
                            allEntries = true)
            })
    public List<InventoryView> updateWhenOrderCancelled(
            OrderCancelledCommand commands) {
        List<Inventory> inventories = loadPort.findAllByListVariantId(
                commands.getDetailCommands().stream().map(item -> item.getVariantId()).toList());

       
        Map<UUID, Inventory> inventoryMap = new HashMap<>();
        List<Inventory> toSaves = new ArrayList<>();
        for (Inventory i : inventories) {
            inventoryMap.put(i.getVariantId().value(), i);
        }
        boolean isShipping = commands.getOrderStatus().value().equals("SHIPPING");

        for (OrderDetailCommand detailCommand : commands.getDetailCommands()) {
            Inventory inventory = inventoryMap.get(detailCommand.getVariantId().value());
            if (inventory == null)
                throw new RuntimeException("Invalid variant id");

            int newQuantity = inventory.getQuantity().value() + detailCommand.getQuantity().value();
            int newReservedQuantity = inventory.getReservedQuantity().value() - detailCommand.getQuantity().value();
            if (isShipping)
                newReservedQuantity = inventory.getReservedQuantity().value();
            if (newReservedQuantity < 0)
                throw new RuntimeException("Invalid info");
            final var updateInfo = Inventory.UpdateInfo.builder().inventoryId(inventory.getId())
                    .quantity(new Quantity(newQuantity)).reservedQuantity(new ReservedQuantity(newReservedQuantity))
                    .status(inventory.getStatus())
                    .build();
            final var toSave = inventory.applyUpdateInfo(updateInfo);
            toSaves.add(toSave);
            

        }

        // publishEventPort.publicUpdateManyInventoriesEvent(new
        // UpdateManyInventoriesEvent(events));

        /*
         * for(OrderDetailCommand command: commands.getDetailCommands()){
         * if(isShipping) {
         * processScript(command.getVariantId().value(), command.getQuantity().value(),
         * redisConfig.getReserveShippingStockScript());
         * }
         * else {
         * processScript(command.getVariantId().value(), command.getQuantity().value(),
         * redisConfig.getCancelStockScript());
         * }
         * }
         */
        savePort.saveAll(toSaves);
        

        return inventories.stream().map(mapper::toView).toList();

    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.INVENTORY,
                            allEntries = true),
                    @CacheEvict(
                            cacheNames = CacheNames.INVENTORY_BY_VARIANT,
                            allEntries = true),
                    @CacheEvict(
                            cacheNames = CacheNames.INVENTORY_LIST,
                            allEntries = true)
            })
    public List<InventoryView> updateWhenOrderShipped(
            OrderShippedCommand commands) {
        List<Inventory> inventories = loadPort.findAllByListVariantId(
                commands.getDetailCommands().stream().map(item -> item.getVariantId()).toList());
        Map<UUID, Inventory> inventoryMap = new HashMap<>();
        List<Inventory> toSaves = new ArrayList<>();
        for (Inventory i : inventories) {
            inventoryMap.put(i.getVariantId().value(), i);
        }
        
        for (OrderDetailCommand detailCommand : commands.getDetailCommands()) {
            Inventory inventory = inventoryMap.get(detailCommand.getVariantId().value());
            if (inventory == null)
                throw new RuntimeException("Invalid variant id");

            int newReservedQuantity = inventory.getReservedQuantity().value() - detailCommand.getQuantity().value();
            if (newReservedQuantity < 0)
                throw new RuntimeException("Invalid info");
            final var updateInfo = Inventory.UpdateInfo.builder().inventoryId(inventory.getId())
                    .quantity(inventory.getQuantity()).reservedQuantity(new ReservedQuantity(newReservedQuantity))
                    .status(inventory.getStatus())
                    .build();
            final var toSave = inventory.applyUpdateInfo(updateInfo);
            
            toSaves.add(toSave);

        }

        // publishEventPort.publicUpdateManyInventoriesEvent(new
        // UpdateManyInventoriesEvent(events));

        /*
         * for(OrderDetailCommand command: commands.getDetailCommands()) {
         * 
         * processScript(command.getVariantId().value(), command.getQuantity().value(),
         * redisConfig.getReleaseStockScript());
         * }
         */
        savePort.saveAll(toSaves);
        

        return inventories.stream().map(mapper::toView).toList();

    }

}
