package vn.uit.edu.msshop.inventory.application.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.adapter.out.event.documents.InventoryUpdatedDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.ForceCancellOrderDocumentRepository;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.InventoryUpdatedDocumentRepository;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderCancelledCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderDetailCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderShippedCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.UpdateInventoryCommand;
import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;
import vn.uit.edu.msshop.inventory.application.exception.InventoryNotFoundException;
import vn.uit.edu.msshop.inventory.application.mapper.InventoryViewMapper;
import vn.uit.edu.msshop.inventory.application.port.in.UpdateInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.out.LoadFromRedisPort;
import vn.uit.edu.msshop.inventory.application.port.out.LoadInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.PublishInventoryEventPort;
import vn.uit.edu.msshop.inventory.application.port.out.SaveInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.SyncInventoryPort;
import vn.uit.edu.msshop.inventory.config.RedisConfig;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.OrderDetail;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.ReservedQuantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class UpdateInventoryService implements UpdateInventoryUseCase {
    private final SaveInventoryPort savePort;
    private final LoadInventoryPort loadPort;
    private final LoadFromRedisPort loadFromRedisPort;
    private final InventoryViewMapper mapper; 
    private final PublishInventoryEventPort publishEventPort;
    private final InventoryUpdatedDocumentRepository inventoryUpdatedDocumentRepo;
    private final ForceCancellOrderDocumentRepository forceCancellOrderDocumentRepo;
    private final RedisTemplate<String,Map<String,String>> redisTemplate;
    private final RedisConfig redisConfig;
    private final SyncInventoryPort syncPort;

    @Override
    @Transactional
    public InventoryView update(UpdateInventoryCommand command) {
        Inventory inventory = loadPort.loadByVariantId(command.variantId()).orElseThrow(()->new InventoryNotFoundException(command.variantId()));
        final var u = Inventory.UpdateInfo.builder().inventoryId(inventory.getId()).quantity(command.quantity().apply(inventory.getQuantity())).reservedQuantity(command.reservedQuantity().apply(inventory.getReservedQuantity())).build();
        final var next = inventory.applyUpdateInfo(u);
        final var saved = savePort.save(next);
        //publishEventPort.publishInventoryUpdateEvent(new InventoryUpdated(saved.getVariantId().value(), saved.getQuantity().value(), saved.getReservedQuantity().value()));
        InventoryUpdatedDocument event = InventoryUpdatedDocument.builder().eventId(UUID.randomUUID())
        .variantId(saved.getVariantId().value())
        .newQuantity(saved.getQuantity().value())
        .newReservedQuantity(saved.getReservedQuantity().value())
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null)
        .isRead(true)
        .build();
        final var savedEvent=inventoryUpdatedDocumentRepo.save(event);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishEventPort.publishInventoryUpdateEvent(savedEvent);
                syncPort.loadFromMainDatabase(saved.getVariantId().value());
            }
        });
        return mapper.toView(saved);
    }

    

    

   

    

    @Override
    @org.springframework.transaction.annotation.Transactional
    public List<InventoryView> updateWhenOrderCancelled(OrderCancelledCommand commands) {
        List<Inventory> inventories = loadFromRedisPort.loadFromRedis(commands.getDetailCommands().stream().map(item->item.getVariantId()).toList());
        List<Inventory> toSaves= new ArrayList<>();
        List<InventoryUpdatedDocument> events = new ArrayList<>();
        boolean isShipping = commands.getOrderStatus().value().equals("SHIPPING");
        
        for(OrderDetailCommand detailCommand: commands.getDetailCommands()) {
            Inventory inventory = findByVariantIdInList(detailCommand.getVariantId(), inventories);
            if(inventory==null) throw new RuntimeException("Invalid variant id");
            
            int newQuantity = inventory.getQuantity().value()+detailCommand.getQuantity().value();
            int newReservedQuantity = inventory.getReservedQuantity().value()-detailCommand.getQuantity().value();
            if(isShipping) newReservedQuantity=inventory.getReservedQuantity().value();
            if(newReservedQuantity<0) throw new RuntimeException("Invalid info");
            final var updateInfo = Inventory.UpdateInfo.builder().inventoryId(inventory.getId()).quantity(new Quantity(newQuantity)).reservedQuantity(new ReservedQuantity(newReservedQuantity)).build();
            final var toSave = inventory.applyUpdateInfo(updateInfo);
            InventoryUpdatedDocument event = InventoryUpdatedDocument.builder().eventId(UUID.randomUUID())
        .variantId(toSave.getVariantId().value())
        .newQuantity(toSave.getQuantity().value())
        .newReservedQuantity(toSave.getReservedQuantity().value())
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null)
        .isRead(false)
        .build();
            events.add(event);
            
        }
       
                
            
        //publishEventPort.publicUpdateManyInventoriesEvent(new UpdateManyInventoriesEvent(events));
        
        
        
        /*for(OrderDetailCommand command: commands.getDetailCommands()){
            if(isShipping) {
                processScript(command.getVariantId().value(), command.getQuantity().value(), redisConfig.getReserveShippingStockScript());
            }
            else {
                processScript(command.getVariantId().value(), command.getQuantity().value(), redisConfig.getCancelStockScript());
            }
        }*/
        List<OrderDetail> details = commands.getDetailCommands().stream().map(item->new OrderDetail(item.getVariantId(),new Quantity(item.getQuantity().value()))).toList();
        if(isShipping) {
            processOrderDetail(details, redisConfig.getReverseShipAllScript());
        }
        else {
            processOrderDetail(details, redisConfig.getCancelAllScript());
        }
        inventoryUpdatedDocumentRepo.saveAll(events);
         TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for(final var event:events) {
                    publishEventPort.publishInventoryUpdateEvent(event);
                }
            }
        });
       
        return toSaves.stream().map(mapper::toView).toList();
    
    
    
    }
    private Inventory findByVariantIdInList(VariantId id, List<Inventory> inventories) {
        for(Inventory inventory:inventories) {
            if(id.value().equals(inventory.getVariantId().value())) return inventory;
        }
        return null;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public List<InventoryView> updateWhenOrderShipped(OrderShippedCommand commands) {
        List<Inventory> inventories = loadFromRedisPort.loadFromRedis(commands.getDetailCommands().stream().map(item->item.getVariantId()).toList());
        List<Inventory> toSaves = new ArrayList<>();
        List<InventoryUpdatedDocument> events = new ArrayList<>();
        for(OrderDetailCommand detailCommand: commands.getDetailCommands()) {
            Inventory inventory = findByVariantIdInList(detailCommand.getVariantId(), inventories);
            if(inventory==null) throw new RuntimeException("Invalid variant id");
            
            
            int newReservedQuantity = inventory.getReservedQuantity().value()-detailCommand.getQuantity().value();
            if(newReservedQuantity<0) throw new RuntimeException("Invalid info");
            final var updateInfo = Inventory.UpdateInfo.builder().inventoryId(inventory.getId()).quantity(inventory.getQuantity()).reservedQuantity(new ReservedQuantity(newReservedQuantity)).build();
            final var toSave = inventory.applyUpdateInfo(updateInfo);
            InventoryUpdatedDocument event = InventoryUpdatedDocument.builder().eventId(UUID.randomUUID())
        .variantId(toSave.getVariantId().value())
        .newQuantity(toSave.getQuantity().value())
        .newReservedQuantity(toSave.getQuantity().value())
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null)
        .isRead(false)
        .build();
            events.add(event);

            
        }
        
            
        //publishEventPort.publicUpdateManyInventoriesEvent(new UpdateManyInventoriesEvent(events));
        
        /*for(OrderDetailCommand command: commands.getDetailCommands()) {
            
            processScript(command.getVariantId().value(), command.getQuantity().value(), redisConfig.getReleaseStockScript());
        }*/
        List<OrderDetail> details = commands.getDetailCommands().stream().map(item->new OrderDetail(item.getVariantId(), new Quantity(item.getQuantity().value()))).toList();
        processOrderDetail(details, redisConfig.getReleaseStockAllScript());
        inventoryUpdatedDocumentRepo.saveAll(events);
         TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for(final var event:events) {
                    publishEventPort.publishInventoryUpdateEvent(event);
                }
            }
        });
    

        return toSaves.stream().map(mapper::toView).toList();
    
    
    
    }

    
public void processOrderDetail(List<OrderDetail> orderDetails, DefaultRedisScript<Long> script) {
        
        List<String> keys = orderDetails.stream()
                .map(item -> "inventory:variant:" + item.getVariantId().toString())
                .toList();
        Object[] args = orderDetails.stream()
                .map(item -> String.valueOf(item.getQuantity().value()))
                .toArray();

        
        Long result = redisTemplate.execute(script, keys, args);

        if (result == null || result == 0||result==-1||result==-2) {
            
            throw new RuntimeException("Loi, vui long xem lai");
        }

        
        System.out.println("Thanh cong");
    }

}
