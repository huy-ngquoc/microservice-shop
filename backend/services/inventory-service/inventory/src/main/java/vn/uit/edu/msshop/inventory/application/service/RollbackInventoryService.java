package vn.uit.edu.msshop.inventory.application.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
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
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.InventoryUpdatedDocumentRepository;
import vn.uit.edu.msshop.inventory.application.exception.InventoryNotFoundException;
import vn.uit.edu.msshop.inventory.application.port.in.RollbackInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.out.LoadFromRedisPort;
import vn.uit.edu.msshop.inventory.application.port.out.PublishInventoryEventPort;
import vn.uit.edu.msshop.inventory.config.RedisConfig;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.OrderDetail;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class RollbackInventoryService implements RollbackInventoryUseCase {
    private final RedisConfig redisConfig;
    private final LoadFromRedisPort loadFromRedisPort;
    private final InventoryUpdatedDocumentRepository inventoryUpdatedRepo;
    private final PublishInventoryEventPort publishPort;
    private final RedisTemplate<String,Map<String,String>> redisTemplate;
    @Override
    @Transactional
    public void orderCreateFail(List<OrderDetail> orderDetails) {
        System.out.println("Tao don hang that bai, rollback");
        List<VariantId> ids = orderDetails.stream().map(item->item.getVariantId()).toList();
        List<Inventory> inventories = loadFromRedisPort.loadFromRedis(ids);
        Map<UUID,Inventory> mapInventory = new HashMap<>();
        for(Inventory i: inventories) {
            mapInventory.put(i.getVariantId().value(), i);
        }
        
        List<InventoryUpdatedDocument> events = new ArrayList<>();
        
        for(OrderDetail o:orderDetails) {
            Inventory i = mapInventory.get(o.getVariantId().value());
            if(i==null) throw new InventoryNotFoundException(o.getVariantId());
            //if(i.getQuantity().value()<o.getQuantity().value()) throw new InsufficientStockException(o.getVariantId());
            
            int newQuantity = i.getQuantity().value()+o.getQuantity().value();
            int newReservedQuantity = i.getReservedQuantity().value()-o.getQuantity().value();
             
            events.add(getInventoryUpdatedDocument(i, newQuantity, newReservedQuantity));
        }
        processOrderDetail(orderDetails,redisConfig.cancellAllScript());
    
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

    @Override
    public void orderShippedFail(List<OrderDetail> orderDetails) {
        List<VariantId> ids = orderDetails.stream().map(item->item.getVariantId()).toList();
        List<Inventory> inventories = loadFromRedisPort.loadFromRedis(ids);
        Map<UUID,Inventory> mapInventory = new HashMap<>();
        for(Inventory i: inventories) {
            mapInventory.put(i.getVariantId().value(), i);
        }
        
        List<InventoryUpdatedDocument> events = new ArrayList<>();
        
        for(OrderDetail o:orderDetails) {
            Inventory i = mapInventory.get(o.getVariantId().value());
            if(i==null) throw new InventoryNotFoundException(o.getVariantId());
            //if(i.getQuantity().value()<o.getQuantity().value()) throw new InsufficientStockException(o.getVariantId());
            
            //int newQuantity = i.getQuantity().value()+o.getQuantity().value();
            int newReservedQuantity = i.getReservedQuantity().value()+o.getQuantity().value();
             
            events.add(getInventoryUpdatedDocument(i, i.getQuantity().value(), newReservedQuantity));
        }
        //processOrderDetail(orderDetails);
    
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

    @Override
    public void orderCancelledFail(List<OrderDetail> orderDetails) {
         List<VariantId> ids = orderDetails.stream().map(item->item.getVariantId()).toList();
        List<Inventory> inventories = loadFromRedisPort.loadFromRedis(ids);
        Map<UUID,Inventory> mapInventory = new HashMap<>();
        for(Inventory i: inventories) {
            mapInventory.put(i.getVariantId().value(), i);
        }
        
        List<InventoryUpdatedDocument> events = new ArrayList<>();
        
        for(OrderDetail o:orderDetails) {
            Inventory i = mapInventory.get(o.getVariantId().value());
            if(i==null) throw new InventoryNotFoundException(o.getVariantId());
            //if(i.getQuantity().value()<o.getQuantity().value()) throw new InsufficientStockException(o.getVariantId());
            
            int newQuantity = i.getQuantity().value()-o.getQuantity().value();
            int newReservedQuantity = i.getReservedQuantity().value()+o.getQuantity().value();
             
            events.add(getInventoryUpdatedDocument(i, newQuantity, newReservedQuantity));
        }
        //processOrderDetail(orderDetails);
    
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

    @Override
    public void orderCancelledWhenShippingFail(List<OrderDetail> orderDetails) {
        List<VariantId> ids = orderDetails.stream().map(item->item.getVariantId()).toList();
        List<Inventory> inventories = loadFromRedisPort.loadFromRedis(ids);
        Map<UUID,Inventory> mapInventory = new HashMap<>();
        for(Inventory i: inventories) {
            mapInventory.put(i.getVariantId().value(), i);
        }
        
        List<InventoryUpdatedDocument> events = new ArrayList<>();
        
        for(OrderDetail o:orderDetails) {
            Inventory i = mapInventory.get(o.getVariantId().value());
            if(i==null) throw new InventoryNotFoundException(o.getVariantId());
            //if(i.getQuantity().value()<o.getQuantity().value()) throw new InsufficientStockException(o.getVariantId());
            
            int newQuantity = i.getQuantity().value()-o.getQuantity().value();
            int newReservedQuantity = i.getReservedQuantity().value();
             
            events.add(getInventoryUpdatedDocument(i, newQuantity, newReservedQuantity));
        }
        //processOrderDetail(orderDetails);
    
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

    private InventoryUpdatedDocument getInventoryUpdatedDocument(Inventory i, int newQuantity, int newReservedQuantity) {
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
        return event;
    }

    public void processOrderDetail(List<OrderDetail> orderDetails, DefaultRedisScript<Long> script) {
        
        List<String> keys = orderDetails.stream()
                .map(item -> "inventory:variant:" + item.getVariantId().value().toString())
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
