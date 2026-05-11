package vn.uit.edu.msshop.inventory.application.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.adapter.in.web.request.OrderOutbox;
import vn.uit.edu.msshop.inventory.adapter.out.event.documents.InventoryUpdatedDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.InventoryUpdatedDocumentRepository;
import vn.uit.edu.msshop.inventory.adapter.out.persistence.ProcessedOrder;
import vn.uit.edu.msshop.inventory.adapter.out.persistence.ProcessedOrderRepository;
import vn.uit.edu.msshop.inventory.application.exception.InsufficientStockException;
import vn.uit.edu.msshop.inventory.application.exception.InventoryNotFoundException;
import vn.uit.edu.msshop.inventory.application.port.in.ProcessOrderUseCase;
import vn.uit.edu.msshop.inventory.application.port.out.LoadInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.PublishInventoryEventPort;
import vn.uit.edu.msshop.inventory.application.port.out.SaveInventoryPort;
import vn.uit.edu.msshop.inventory.bootstrap.config.cache.CacheNames;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.OrderDetail;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.ReservedQuantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class ProcessOrderService implements ProcessOrderUseCase {
    private final InventoryUpdatedDocumentRepository inventoryUpdatedRepo;
    private final PublishInventoryEventPort publishPort;
    private final LoadInventoryPort loadPort;
    private final SaveInventoryPort savePort;
    private final ProcessedOrderRepository processedOrderRepo;

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
    public void processOrder(
            List<OrderDetail> orderDetails) {

        List<VariantId> ids = orderDetails.stream().map(item -> item.getVariantId()).toList();
        List<Inventory> inventories = loadPort.findByListVariantId(ids);
        Map<UUID, Inventory> inventoryMap = new HashMap<>();
        List<Inventory> toSaves = new ArrayList<>();
        for (Inventory i : inventories) {
            inventoryMap.put(i.getVariantId().value(), i);
        }
        List<InventoryUpdatedDocument> events = new ArrayList<>();

        for (OrderDetail o : orderDetails) {
            Inventory i = inventoryMap.get(o.getVariantId().value());
            if(i==null) throw new InventoryNotFoundException(o.getVariantId());
            if(i.getQuantity().value()<o.getQuantity().value()) throw new InsufficientStockException(o.getVariantId());
            
            int newQuantity = i.getQuantity().value()-o.getQuantity().value();
            int newReservedQuantity = i.getReservedQuantity().value()+o.getQuantity().value();
            if(newQuantity<0) throw new InsufficientStockException(o.getVariantId());

            final var updateInfo = Inventory.UpdateInfo.builder().inventoryId(i.getId()).quantity(new Quantity(newQuantity)).reservedQuantity(new ReservedQuantity(newReservedQuantity))
            .status(i.getStatus())
            .build();
            final var toSave = i.applyUpdateInfo(updateInfo);

            InventoryUpdatedDocument event = InventoryUpdatedDocument.builder().eventId(UUID.randomUUID())
                    .variantId(i.getVariantId().value())
                    .newQuantity(newQuantity)
                    .newReservedQuantity(newReservedQuantity)
                    .eventStatus("PENDING")
                    .retryCount(0)
                    .createdAt(Instant.now())
                    .updatedAt(null)
                    .lastError(null)
                    .isRead(false)
                    .build();
            events.add(event);
            toSaves.add(toSave);
        }

        inventoryUpdatedRepo.saveAll(events);
        savePort.saveAll(toSaves);
        // System.out.println("Tao don hang thanh cong");

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
    public void processOrderOutbox(
            OrderOutbox outbox) {
        if (processedOrderRepo.existsById(outbox.getId()))
            throw new RuntimeException("Trung du lieu");
        List<OrderDetail> details = outbox.getRequests().stream()
                .map(item -> new OrderDetail(new VariantId(item.getVariantId()), new Quantity(item.getQuantity())))
                .toList();

        processedOrderRepo.save(new ProcessedOrder(outbox.getId(), outbox.getOutboxStatus()));

        processOrder(details);

    }
}
