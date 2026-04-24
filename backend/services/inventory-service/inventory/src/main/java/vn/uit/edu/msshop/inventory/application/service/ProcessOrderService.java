package vn.uit.edu.msshop.inventory.application.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.adapter.out.event.documents.InventoryUpdatedDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.InventoryUpdatedDocumentRepository;
import vn.uit.edu.msshop.inventory.application.exception.InsufficientStockException;
import vn.uit.edu.msshop.inventory.application.exception.InventoryNotFoundException;
import vn.uit.edu.msshop.inventory.application.port.in.ProcessOrderUseCase;
import vn.uit.edu.msshop.inventory.application.port.out.LoadFromRedisPort;
import vn.uit.edu.msshop.inventory.application.port.out.PublishInventoryEventPort;
import vn.uit.edu.msshop.inventory.application.port.out.SaveInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.SyncInventoryPort;
import vn.uit.edu.msshop.inventory.config.RedisConfig;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.OrderDetail;
import vn.uit.edu.msshop.inventory.domain.model.OrderProcessJob;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class ProcessOrderService implements ProcessOrderUseCase {
    private final LoadFromRedisPort loadFromRedisPort;
    private final RedisTemplate<String,Map<String,String>> redisTemplate;
    private final RedisConfig redisConfig;
    private final SyncInventoryPort syncPort;
    private final InventoryUpdatedDocumentRepository inventoryUpdatedRepo;
    private final PublishInventoryEventPort publishPort;
    private final SaveInventoryPort savePort;
    
    @Override
    @Transactional
    public void processOrder(List<OrderDetail> orderDetails) {
        List<VariantId> ids = orderDetails.stream().map(item->item.getVariantId()).toList();
        List<Inventory> inventories = loadFromRedisPort.loadFromRedis(ids);
        List<OrderProcessJob> orderProcessJobs = new ArrayList<>();
        List<InventoryUpdatedDocument> events = new ArrayList<>();
        
        for(OrderDetail o:orderDetails) {
            Inventory i = findInList(inventories, o.getVariantId());
            if(i==null) throw new InventoryNotFoundException(o.getVariantId());
            //if(i.getQuantity().value()<o.getQuantity().value()) throw new InsufficientStockException(o.getVariantId());
            orderProcessJobs.add(new OrderProcessJob(i,o.getQuantity()));
            int newQuantity = i.getQuantity().value()-o.getQuantity().value();
            int newReservedQuantity = i.getReservedQuantity().value()+o.getQuantity().value();
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
        }
        processOrderDetail(orderDetails);
    
        inventoryUpdatedRepo.saveAll(events);
         TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for(final var event:events) {
                    publishPort.publishInventoryUpdateEvent(event);
                }
            }
        });
    


    }
    private Inventory findInList(List<Inventory> inventories, VariantId variantId) {
        for(Inventory i: inventories) {
            if(i.getVariantId().value().equals(variantId.value())) return i;
        }
        return null;
    }
    public void processOrderDetail(List<OrderDetail> orderDetails) {
        
        List<String> keys = orderDetails.stream()
                .map(item -> "inventory:variant:" + item.getVariantId().toString())
                .toList();
        Object[] args = orderDetails.stream()
                .map(item -> String.valueOf(item.getQuantity().value()))
                .toArray();

        
        Long result = redisTemplate.execute(redisConfig.reserveAllScript(), keys, args);

        if (result == null || result == 0) {
            
            throw new InsufficientStockException("Một hoặc nhiều sản phẩm không đủ tồn kho!");
        }

        
        System.out.println("Đã giữ chỗ thành công cho toàn bộ đơn hàng trên Redis");
    }
}
    
    

