package vn.uit.edu.msshop.inventory.application.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.adapter.out.event.ForceCancellOrderDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.ForceCancellOrderDocumentRepository;
import vn.uit.edu.msshop.inventory.adapter.out.event.InventoryUpdatedDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.InventoryUpdatedDocumentRepository;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderCancelledCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderCreateCommand;
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
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.ReservedQuantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class UpdateInventoryService implements UpdateInventoryUseCase {
    private final SaveInventoryPort savePort;
    private final LoadInventoryPort loadPort;
    private final InventoryViewMapper mapper; 
    private final PublishInventoryEventPort publishEventPort;
    private final InventoryUpdatedDocumentRepository inventoryUpdatedDocumentRepo;
    private final ForceCancellOrderDocumentRepository forceCancellOrderDocumentRepo;

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
        .build();
        final var savedEvent=inventoryUpdatedDocumentRepo.save(event);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishEventPort.publishInventoryUpdateEvent(savedEvent);
            }
        });
        return mapper.toView(saved);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public List<InventoryView> updateWhenOrderCreated(OrderCreateCommand commands) {
        List<Inventory> inventories = loadPort.findByListVariantId(commands.getDetailCommands().stream().map(item->item.getVariantId()).toList());
        System.out.println("Inventories size "+inventories.get(0).getVariantId().value().toString() );
        List<Inventory> toSaves = new ArrayList<>();
        List<InventoryUpdatedDocument> events = new ArrayList<>();
        for(OrderDetailCommand detailCommand: commands.getDetailCommands()) {
            System.out.println(detailCommand.getVariantId().value());
            Inventory inventory = findByVariantIdInList(detailCommand.getVariantId(), inventories);
            if(inventory==null) throw new RuntimeException("Invalid variant id");
            if(inventory.getQuantity().value()<detailCommand.getQuantity().value()) {

                //publishEventPort.publishForceCancellOrderEvent(new ForceCancellOrder(commands.getOrderId()));
                final var outboxEvent = forceCancellOrderDocumentRepo.save(ForceCancellOrderDocument.builder().orderId(commands.getOrderId()).eventId(UUID.randomUUID()).build());
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                
                publishEventPort.publishForceCancellOrderEvent(outboxEvent);
                
            }
        });
                return null;
            }
            int newQuantity = inventory.getQuantity().value()-detailCommand.getQuantity().value();
            int newReservedQuantity = inventory.getReservedQuantity().value()+detailCommand.getQuantity().value();
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
        .build();
            events.add(event);
            toSaves.add(toSave);
        }
         TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for(final var event:events) {
                    publishEventPort.publishInventoryUpdateEvent(event);
                }
            }
        });
        //publishEventPort.publicUpdateManyInventoriesEvent(new UpdateManyInventoriesEvent(events));
        inventoryUpdatedDocumentRepo.saveAll(events);
        return savePort.saveAll(toSaves).stream().map(mapper::toView).toList();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public List<InventoryView> updateWhenOrderCancelled(OrderCancelledCommand commands) {
        List<Inventory> inventories = loadPort.findByListVariantId(commands.getDetailCommands().stream().map(item->item.getVariantId()).toList());
        List<Inventory> toSaves = new ArrayList<>();
        List<InventoryUpdatedDocument> events = new ArrayList<>();
        for(OrderDetailCommand detailCommand: commands.getDetailCommands()) {
            Inventory inventory = findByVariantIdInList(detailCommand.getVariantId(), inventories);
            if(inventory==null) throw new RuntimeException("Invalid variant id");
            
            int newQuantity = inventory.getQuantity().value()+detailCommand.getQuantity().value();
            int newReservedQuantity = inventory.getReservedQuantity().value()-detailCommand.getQuantity().value();
            if(commands.getOrderStatus().value().equals("SHIPPING")) newReservedQuantity=inventory.getReservedQuantity().value();
            if(newReservedQuantity<0) throw new RuntimeException("Invalid info");
            final var updateInfo = Inventory.UpdateInfo.builder().inventoryId(inventory.getId()).quantity(new Quantity(newQuantity)).reservedQuantity(new ReservedQuantity(newReservedQuantity)).build();
            final var toSave = inventory.applyUpdateInfo(updateInfo);
            InventoryUpdatedDocument event = InventoryUpdatedDocument.builder()
        .variantId(toSave.getVariantId().value())
        .newQuantity(toSave.getQuantity().value())
        .newReservedQuantity(0)
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null)
        .build();
            events.add(event);
            toSaves.add(toSave);
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for(final var event:events) {
                    publishEventPort.publishInventoryUpdateEvent(event);
                }
            }
        });
        //publishEventPort.publicUpdateManyInventoriesEvent(new UpdateManyInventoriesEvent(events));
        inventoryUpdatedDocumentRepo.saveAll(events);
        return savePort.saveAll(toSaves).stream().map(mapper::toView).toList();
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
        List<Inventory> inventories = loadPort.findByListVariantId(commands.getDetailCommands().stream().map(item->item.getVariantId()).toList());
        List<Inventory> toSaves = new ArrayList<>();
        List<InventoryUpdatedDocument> events = new ArrayList<>();
        for(OrderDetailCommand detailCommand: commands.getDetailCommands()) {
            Inventory inventory = findByVariantIdInList(detailCommand.getVariantId(), inventories);
            if(inventory==null) throw new RuntimeException("Invalid variant id");
            
            
            int newReservedQuantity = inventory.getReservedQuantity().value()-detailCommand.getQuantity().value();
            if(newReservedQuantity<0) throw new RuntimeException("Invalid info");
            final var updateInfo = Inventory.UpdateInfo.builder().inventoryId(inventory.getId()).quantity(inventory.getQuantity()).reservedQuantity(new ReservedQuantity(newReservedQuantity)).build();
            final var toSave = inventory.applyUpdateInfo(updateInfo);
            InventoryUpdatedDocument event = InventoryUpdatedDocument.builder()
        .variantId(toSave.getVariantId().value())
        .newQuantity(toSave.getQuantity().value())
        .newReservedQuantity(0)
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null)
        .build();
            events.add(event);

            toSaves.add(toSave);
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for(final var event:events) {
                    publishEventPort.publishInventoryUpdateEvent(event);
                }
            }
        });
        //publishEventPort.publicUpdateManyInventoriesEvent(new UpdateManyInventoriesEvent(events));
        inventoryUpdatedDocumentRepo.saveAll(events);
        return savePort.saveAll(toSaves).stream().map(mapper::toView).toList();
    }

}
