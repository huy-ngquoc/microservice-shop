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
import vn.uit.edu.msshop.inventory.adapter.out.event.documents.ForceCancellOrderDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.documents.InventoryUpdatedDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.ForceCancellOrderDocumentRepository;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.InventoryUpdatedDocumentRepository;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderCancelledCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderCreateCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderDetailCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderShippedCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.UpdateInventoryCommand;
import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;
import vn.uit.edu.msshop.inventory.application.exception.InsufficientStockException;
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
    public List<InventoryView> updateWhenOrderCreated(OrderCreateCommand commands) {
        List<Inventory> inventories = loadFromRedisPort.loadFromRedis(commands.getDetailCommands().stream().map(item->item.getVariantId()).toList());
        //System.out.println("Inventories size "+inventories.get(0).getVariantId().value().toString() );
        List<Inventory> toSaves = new ArrayList<>();
        List<InventoryUpdatedDocument> events = new ArrayList<>();
        for(OrderDetailCommand detailCommand: commands.getDetailCommands()) {
            //System.out.println(detailCommand.getVariantId().value());
            Inventory inventory = findByVariantIdInList(detailCommand.getVariantId(), inventories);
            if(inventory==null) throw new RuntimeException("Invalid variant id");
            if(inventory.getQuantity().value()<detailCommand.getQuantity().value()) {

                //publishEventPort.publishForceCancellOrderEvent(new ForceCancellOrder(commands.getOrderId()));
                final var outboxEvent = forceCancellOrderDocumentRepo.save(ForceCancellOrderDocument.builder().orderId(commands.getOrderId()).eventId(UUID.randomUUID()).build());
                publishEventPort.publishForceCancellOrderEvent(outboxEvent);
                return null;
            }
            int newQuantity = inventory.getQuantity().value()-detailCommand.getQuantity().value();
            int newReservedQuantity = inventory.getReservedQuantity().value()+detailCommand.getQuantity().value();
            final var updateInfo = Inventory.UpdateInfo.builder().inventoryId(inventory.getId()).quantity(new Quantity(newQuantity)).reservedQuantity(new ReservedQuantity(newReservedQuantity)).build();
            final var toSave = inventory.applyUpdateInfo(updateInfo);
            toSaves.add(toSave);
            InventoryUpdatedDocument event = InventoryUpdatedDocument.builder().eventId(UUID.randomUUID())
        .variantId(toSave.getVariantId().value())
        .newQuantity(toSave.getQuantity().value())
        .newReservedQuantity(0)
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
        inventoryUpdatedDocumentRepo.saveAll(events);
        for(OrderDetailCommand command: commands.getDetailCommands()) {
            //System.out.println("Call check and reserve, amount "+command.getQuantity().value());
            processScript(command.getVariantId().value(), command.getQuantity().value(), redisConfig.getReserveStockScript());
        }
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

    

    public void processScript(UUID variantId, int amount, DefaultRedisScript<Long> script) {
        String key = "inventory:variant:" + variantId.toString();
    
    Long result = redisTemplate.execute(script, List.of(key), String.valueOf(amount));

    if (result == -1) {
        // Tình huống: Redis trống trơn (hết hạn hoặc chưa nạp)
        //System.out.println("Redis miss! Loading from DB...");
        Inventory inventory = syncPort.loadFromMainDatabase(variantId);
        if(inventory==null) throw new InventoryNotFoundException(new VariantId(variantId));
        // Sau khi load xong thì gọi lại chính nó (Recursive) hoặc báo user thử lại
        processScript(variantId, amount, script); 
    } else if (result == 0) {
        throw new InsufficientStockException(new VariantId(variantId));
    } else if (result == -2) {
        throw new IllegalStateException("Dữ liệu tồn kho trên Redis bị lỗi định dạng!");
    } else if (result == 1) {
        
    }
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
        .newReservedQuantity(0)
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null)
        .isRead(false)
        .build();
            events.add(event);
            
        }
       
                for(final var event:events) {
                    publishEventPort.publishInventoryUpdateEvent(event);
                }
            
        //publishEventPort.publicUpdateManyInventoriesEvent(new UpdateManyInventoriesEvent(events));
        inventoryUpdatedDocumentRepo.saveAll(events);
        
        for(OrderDetailCommand command: commands.getDetailCommands()){
            if(isShipping) {
                processScript(command.getVariantId().value(), command.getQuantity().value(), redisConfig.getReserveShippingStockScript());
            }
            else {
                processScript(command.getVariantId().value(), command.getQuantity().value(), redisConfig.getCancelStockScript());
            }
        }
       
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
        .newReservedQuantity(0)
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null)
        .isRead(false)
        .build();
            events.add(event);

            
        }
        
                for(final var event:events) {
                    publishEventPort.publishInventoryUpdateEvent(event);
                }
            
        //publishEventPort.publicUpdateManyInventoriesEvent(new UpdateManyInventoriesEvent(events));
        
        for(OrderDetailCommand command: commands.getDetailCommands()) {
            
            processScript(command.getVariantId().value(), command.getQuantity().value(), redisConfig.getReleaseStockScript());
        }
        
        inventoryUpdatedDocumentRepo.saveAll(events);
        return toSaves.stream().map(mapper::toView).toList();
    
    
    }

    private InventoryUpdatedDocument createUpdateEvent(Inventory inventory) {
    return InventoryUpdatedDocument.builder()
        .eventId(UUID.randomUUID())
        .variantId(inventory.getVariantId().value())
        .newQuantity(inventory.getQuantity().value())
        .newReservedQuantity(inventory.getReservedQuantity().value())
        .eventStatus("PENDING")
        .createdAt(Instant.now())
        .build();
}
private void handleForceCancel(UUID orderId) {
    var cancelEvent = ForceCancellOrderDocument.builder()
            .orderId(orderId).eventId(UUID.randomUUID()).build();
    forceCancellOrderDocumentRepo.save(cancelEvent);
    publishEventPort.publishForceCancellOrderEvent(cancelEvent);
}

}
