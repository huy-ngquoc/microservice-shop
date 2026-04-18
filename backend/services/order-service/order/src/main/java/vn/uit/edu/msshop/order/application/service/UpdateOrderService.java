package vn.uit.edu.msshop.order.application.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.out.event.documents.CodPaymentCancelledDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.CodPaymentReceivedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.IncreaseSoldCountDetailDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.IncreaseSoldCountEventsDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.inventory.OrderCancelledDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.inventory.OrderDetailDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.inventory.OrderShippedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.CodPaymentCancelledDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.CodPaymentReceivedDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.IncreaseSoldCountEventDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.inventory.OrderCancelledDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.inventory.OrderShippedDocumentRepository;
import vn.uit.edu.msshop.order.application.dto.command.IncreaseVariantSoldCountCommand;
import vn.uit.edu.msshop.order.application.dto.command.UpdateOrderCommand;
import vn.uit.edu.msshop.order.application.exception.OrderNotFoundException;
import vn.uit.edu.msshop.order.application.port.in.CheckPermissionUseCase;
import vn.uit.edu.msshop.order.application.port.in.UpdateOrderUseCase;
import vn.uit.edu.msshop.order.application.port.in.UpdateVariantSoldCountUseCase;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderPort;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.domain.event.OrderUpdated;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.Amount;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;
/*@NonNull
        OrderId  id,
        ShippingInfo shippingInfo,
        OrderStatus orderStatus */
@Service
@RequiredArgsConstructor
public class UpdateOrderService implements UpdateOrderUseCase {
    private final LoadOrderPort loadOrderPort;
    private final SaveOrderPort saveOrderPort;
    private final PublishOrderEventPort publishEventPort;
    private final CheckPermissionUseCase checkPermission;
    private final CodPaymentCancelledDocumentRepository codPaymentCancelledDocumentRepo;
    private final CodPaymentReceivedDocumentRepository codPaymentReceivedDocumentRepo;
    private final OrderCancelledDocumentRepository orderCancelledDocumentRepo;
    private final OrderShippedDocumentRepository orderShippedDocumentRepo;
    private final UpdateVariantSoldCountUseCase updateVariantSoldCountUseCase;
    private final IncreaseSoldCountEventDocumentRepository increaseSoldCountEventDocumentRepo;
    

    @Override
    @Transactional
    public void update(UpdateOrderCommand command, String userIdFromHeader, String role) {
        
        Order order = loadOrderPort.loadById(command.id()).orElseThrow(()->new OrderNotFoundException(command.id()));
        if(!checkPermission.isAdmin(role)&&!checkPermission.isSameUser(userIdFromHeader, order.getUserId().value().toString())) {
            throw new RuntimeException("Unauthorized");
        }
        System.out.println("Order currency is "+order.getCurrency().value());
        String oldStatus = order.getStatus().value();
        final var updateInfo = Order.UpdateInfo.builder().id(command.id()).shippingInfo(command.shippingInfo().apply(order.getShippingInfo())).orderStatus(command.status().apply(order.getStatus()))
        .build();
        final var next = order.applyUpdateInfo(updateInfo);
        final var saved = saveOrderPort.save(next);
        boolean isSendEvent = !oldStatus.equals(saved.getStatus().value());
        CodPaymentCancelledDocument savedCodPaymentCancelledDocument = null;
        OrderCancelledDocument savedOrderCancelledDocument=null;
        IncreaseSoldCountEventsDocument savedIncreaseSoldCountEventDocument=null;
        CodPaymentReceivedDocument savedCodPaymentReceivedDocument=null;
        OrderShippedDocument savedOrderShippedDocument=null;
       
        
        if(saved.getStatus().value().equals("CANCELLED")&&isSendEvent) {
            CodPaymentCancelledDocument outboxEvent = CodPaymentCancelledDocument.builder().eventId(UUID.randomUUID())
            .orderId(saved.getId().value())
            .eventStatus("PENDING")
            .retryCount(0)
            .createdAt(Instant.now())
            .updatedAt(null)
            .lastError(null).build();
            savedCodPaymentCancelledDocument = codPaymentCancelledDocumentRepo.save(outboxEvent);
            
        OrderCancelledDocument outboxOrderCancelled = OrderCancelledDocument.builder().eventId(UUID.randomUUID())
        .orderId(saved.getId().value())
        .oldStatus(oldStatus)
        .orderDetails(saved.getDetails().stream().map(item->new OrderDetailDocument(item.variantId(), item.amount())).toList())
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null).build();
         savedOrderCancelledDocument= orderCancelledDocumentRepo.save(outboxOrderCancelled);
        
           // publishEventPort.publishOrderCancelled_InventoryEvent(new OrderCancelled(saved.getId().value(),listEvent,oldStatus));
        }
        if(saved.getStatus().value().equals("RECEIVED")) {
            final var commands = saved.getDetails().stream().map(item->new IncreaseVariantSoldCountCommand(
                new VariantId(item.variantId()), new Amount(item.amount())
            )).toList();
            updateVariantSoldCountUseCase.updateMany(commands);
            IncreaseSoldCountEventsDocument eventsDocument = IncreaseSoldCountEventsDocument.builder().eventId(UUID.randomUUID())
            .details(saved.getDetails().stream().map(item->new IncreaseSoldCountDetailDocument(item.variantId(),item.amount())).toList())
            .eventStatus("PENDING")
            .retryCount(0)
            .createdAt(Instant.now())
            .updatedAt(null).build();
            savedIncreaseSoldCountEventDocument = increaseSoldCountEventDocumentRepo.save(eventsDocument);
            
        }
        if(saved.getStatus().value().equals("RECEIVED")&&isSendEvent) {
            CodPaymentReceivedDocument outboxEvent = CodPaymentReceivedDocument.builder().eventId(UUID.randomUUID())
        .orderId(saved.getId().value())
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null).build();
        savedCodPaymentReceivedDocument= codPaymentReceivedDocumentRepo.save(outboxEvent);
        
            //publishEventPort.publishCodPaymentReceived(new CodPaymentReceived(saved.getId().value()));
        }
        if(saved.getStatus().value().equals("SHIPPING")&&isSendEvent) {
            OrderShippedDocument orderShippedDocument = OrderShippedDocument.builder().eventId(UUID.randomUUID())
            .orderId(saved.getId().value())
            .orderDetails(saved.getDetails().stream().map(item->new OrderDetailDocument(item.variantId(), item.amount())).toList())
            .eventStatus("PENDING")
            .retryCount(0)
            .createdAt(Instant.now())
            .updatedAt(null)
            .lastError(null).build();
            savedOrderShippedDocument = orderShippedDocumentRepo.save(orderShippedDocument);
            
            //publishEventPort.publishOrderShipped_InventoryEvent(new OrderShipped(saved.getId().value(),listEvent));
        }
        final var codPaymentCancelledDocument = savedCodPaymentCancelledDocument;
        final var orderCancelledDocument = savedOrderCancelledDocument;
        final var increaseSoldCountEventDocument = savedIncreaseSoldCountEventDocument;
        final var codPaymentReceivedDocument = savedCodPaymentReceivedDocument;
        final var orderShippedDocument= savedOrderShippedDocument;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                /*
                CodPaymentCancelledDocument savedCodPaymentCancelledDocument = null;
        OrderCancelledDocument savedOrderCancelledDocument=null;
        IncreaseSoldCountEventsDocument savedIncreaseSoldCountEventDocument=null;
        CodPaymentReceivedDocument savedCodPaymentReceivedDocument=null;
        OrderShippedDocument savedOrderShippedDocument=null; */
                if(codPaymentCancelledDocument!=null) {
                    publishEventPort.publishCodPaymentCancelled(codPaymentCancelledDocument);
                }
                if(orderCancelledDocument!=null) {
                    publishEventPort.publishOrderCancelled_InventoryEvent(orderCancelledDocument);
                }
                if(increaseSoldCountEventDocument!=null) {
                    publishEventPort.publishIncreaseSoldCountEvent(increaseSoldCountEventDocument);
                }
                if(codPaymentReceivedDocument!=null){
                    publishEventPort.publishCodPaymentReceived(codPaymentReceivedDocument);
                }
                if(orderShippedDocument!=null) {
                    publishEventPort.publishOrderShipped_InventoryEvent(orderShippedDocument);
                }
            }
        });
        publishEventPort.publish(new OrderUpdated(saved.getId()));
    }

    @Override
    @Transactional
    public void codOrderSuccess(OrderId orderId, String userIdFromHeader) {

        Order order = loadOrderPort.loadById(orderId).orElseThrow(()->new OrderNotFoundException(orderId));
        if(!checkPermission.isSameUser(userIdFromHeader, order.getUserId().value().toString())) {
            throw new RuntimeException("Unauthorized");
        }
        final var updateInfo = Order.UpdateInfo.builder().id(order.getId()).shippingInfo(order.getShippingInfo()).orderStatus(new OrderStatus("RECEIVED")).build();
        final var saved = saveOrderPort.save(order.applyUpdateInfo(updateInfo));
        CodPaymentReceivedDocument outboxEvent = CodPaymentReceivedDocument.builder().eventId(UUID.randomUUID())
        .orderId(saved.getId().value())
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null).build();
        final var savedEvent = codPaymentReceivedDocumentRepo.save(outboxEvent);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishEventPort.publishCodPaymentReceived(savedEvent);
            }
        });
        
    }

    @Override
    @Transactional
    public void codOrderCancelled(OrderId orderId) {
       Order order = loadOrderPort.loadById(orderId).orElseThrow(()->new OrderNotFoundException(orderId));
       String oldStatus = order.getStatus().value();
        final var updateInfo = Order.UpdateInfo.builder().id(order.getId()).shippingInfo(order.getShippingInfo()).orderStatus(new OrderStatus("CANCELLED")).build();
        final var saved = saveOrderPort.save(order.applyUpdateInfo(updateInfo));
        //final var listEvent = saved.getDetails().stream().map(item->new OrderDetail(item.variantId(),item.amount())).toList();
        CodPaymentCancelledDocument outboxEvent = CodPaymentCancelledDocument.builder().eventId(UUID.randomUUID())
            .orderId(saved.getId().value())
            .eventStatus("PENDING")
            .retryCount(0)
            .createdAt(Instant.now())
            .updatedAt(null)
            .lastError(null).build();
            final var savedEvent = codPaymentCancelledDocumentRepo.save(outboxEvent);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishEventPort.publishCodPaymentCancelled(savedEvent);
            }
        });
        OrderCancelledDocument outboxOrderCancelled = OrderCancelledDocument.builder().eventId(UUID.randomUUID())
        .orderId(saved.getId().value())
        .oldStatus(oldStatus)
        .orderDetails(saved.getDetails().stream().map(item->new OrderDetailDocument(item.variantId(), item.amount())).toList())
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null).build();
        final var savedOutboxOrderCancelled = orderCancelledDocumentRepo.save(outboxOrderCancelled);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishEventPort.publishOrderCancelled_InventoryEvent(savedOutboxOrderCancelled);
            }
        });
        //publishEventPort.publishOrderCancelled_InventoryEvent(new OrderCancelled(saved.getId().value(),listEvent,oldStatus));
    }

    @Override
    @Transactional
    public void forceCancellOrder(OrderId orderId) {
        Order order = loadOrderPort.loadById(orderId).orElseThrow(()->new OrderNotFoundException(orderId));
        final var updateInfo = Order.UpdateInfo.builder().id(order.getId()).shippingInfo(order.getShippingInfo()).orderStatus(new OrderStatus("CANCELLED")).build();
        saveOrderPort.save(order.applyUpdateInfo(updateInfo));
    }

}
