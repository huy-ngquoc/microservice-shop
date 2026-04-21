package vn.uit.edu.msshop.order.application.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.out.event.documents.CodPaymentCancelledDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.CodPaymentReceivedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderUpdatedEventDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.CodPaymentCancelledDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.CodPaymentReceivedDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderUpdatedRepository;
import vn.uit.edu.msshop.order.application.dto.command.UpdateOrderCommand;
import vn.uit.edu.msshop.order.application.exception.OrderNotFoundException;
import vn.uit.edu.msshop.order.application.exception.WrongUpdateInfoException;
import vn.uit.edu.msshop.order.application.port.in.CheckPermissionUseCase;
import vn.uit.edu.msshop.order.application.port.in.UpdateOrderUseCase;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderPort;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.domain.event.OrderDetailEvent;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.PaymentStatus;
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
   
    private final OrderUpdatedRepository orderUpdatedRepo;
    /*
    private UUID eventId;
    private UUID orderId;

    private List<OrderDetailEvent> details;
    
    private String status;
    
    private UUID userId;

    private long originPrice;

    private long shippingFee;

    private long discount;

    private long totalPrice;
    private String currency;
    private String paymentMethod;
    private String paymentStatus;
    private String email; */
    

    @Override
    @Transactional
    public void update(UpdateOrderCommand command, String userIdFromHeader, String role) {
        
        Order order = loadOrderPort.loadById(command.id()).orElseThrow(()->new OrderNotFoundException(command.id()));
        if(!checkPermission.isAdmin(role)&&!checkPermission.isSameUser(userIdFromHeader, order.getUserId().value().toString())) {
            throw new RuntimeException("Unauthorized");
        }
        System.out.println("Order currency is "+order.getCurrency().value());
        String oldStatus = order.getStatus().value();
        
        String commandStatus = command.status().apply(order.getStatus()).value();
        if(!oldStatus.equals(commandStatus)) {
            if(!preUpdateCheck(order, new OrderStatus(commandStatus))) {
                throw new WrongUpdateInfoException("Invalid order status");
            }
        }
        final var updateInfo = Order.UpdateInfo.builder().id(command.id()).shippingInfo(command.shippingInfo().apply(order.getShippingInfo())).orderStatus(command.status().apply(order.getStatus()))
        .build();
        var next = order.applyUpdateInfo(updateInfo);
        if(next.getStatus().value().equals("CANCELLED")) {
            next= next.updatePaymentStatus(new PaymentStatus("CANCELLED"));
        }
        final var saved = saveOrderPort.save(next);
        boolean isSendEvent = !oldStatus.equals(saved.getStatus().value());
        if(isSendEvent) {
            System.out.println("Send eventtttttttttttttttttttttttttttttttt");
            List<OrderDetailEvent> detailEvents = saved.getDetails().stream().map(item->new OrderDetailEvent(item.variantId(), item.productId(), item.amount())).toList();
            OrderUpdatedEventDocument eventDocument = OrderUpdatedEventDocument.builder()
            .eventId(UUID.randomUUID())
            .orderId(saved.getId().value())
            .details(detailEvents)
            .status(saved.getStatus().value())
            .userId(saved.getUserId().value())
            .originPrice(saved.getOriginPrice().value())
            .shippingFee(saved.getShippingFee().value())
            .discount(saved.getDiscount().value())
            .totalPrice(saved.getTotalPrice().value())
            .currency(saved.getCurrency().value())
            .paymentMethod(saved.getPaymentMethod().value())
            .paymentStatus(saved.getPaymentStatus().value())
            .email(saved.getShippingInfo().email())
            .oldStatus(oldStatus)
            .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null).build();
        final var savedEvent = orderUpdatedRepo.save(eventDocument);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishEventPort.publishOrderUpdatedEvent(savedEvent);
            }
        });
        }
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
       /*  OrderCancelledDocument outboxOrderCancelled = OrderCancelledDocument.builder().eventId(UUID.randomUUID())
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
        });*/
        //publishEventPort.publishOrderCancelled_InventoryEvent(new OrderCancelled(saved.getId().value(),listEvent,oldStatus));
    }

    @Override
    @Transactional
    public void forceCancellOrder(OrderId orderId) {
        Order order = loadOrderPort.loadById(orderId).orElseThrow(()->new OrderNotFoundException(orderId));
        final var updateInfo = Order.UpdateInfo.builder().id(order.getId()).shippingInfo(order.getShippingInfo()).orderStatus(new OrderStatus("CANCELLED")).build();
        saveOrderPort.save(order.applyUpdateInfo(updateInfo));
    }
    private boolean preUpdateCheck(Order order, OrderStatus newStatus) {
        String newStatusValue = newStatus.getValue();
        String oldStatusValue = order.getStatus().value();
        String paymentStatus = order.getPaymentStatus().value();
        String paymentMethod = order.getPaymentMethod().value();
        if(newStatusValue.equals("PENDING")) return false;
        if(newStatusValue.equals("SHIPPING")) {
            if(!oldStatusValue.equals("PENDING")) return false;
            if(paymentMethod.equals("ONLINE")&&!paymentStatus.equals("SUCCESS")) return false;
        }
        if(newStatusValue.equals("CANCELLED")) {
            if(oldStatusValue.equals("RECEIVED")) return false;
        }
        if(newStatusValue.equals("RECEIVED")) {
            if(!oldStatusValue.equals("SHIPPING")) return false;
        }
        return true;
    }

}
