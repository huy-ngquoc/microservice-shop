package vn.uit.edu.msshop.order.application.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.out.event.CodPaymentCancelledDocument;
import vn.uit.edu.msshop.order.adapter.out.event.CodPaymentCancelledDocumentRepository;
import vn.uit.edu.msshop.order.application.dto.command.UpdateOrderCommand;
import vn.uit.edu.msshop.order.application.exception.OrderNotFoundException;
import vn.uit.edu.msshop.order.application.port.in.CheckPermissionUseCase;
import vn.uit.edu.msshop.order.application.port.in.UpdateOrderUseCase;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderPort;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.domain.event.CodPaymentReceived;
import vn.uit.edu.msshop.order.domain.event.OrderUpdated;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderCancelled;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderDetail;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderShipped;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
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

    @Override
    public void update(UpdateOrderCommand command, String userIdFromHeader, String role) {
        
        Order order = loadOrderPort.loadById(command.id()).orElseThrow(()->new OrderNotFoundException(command.id()));
        if(!checkPermission.isAdmin(role)&&!checkPermission.isSameUser(userIdFromHeader, order.getUserId().value().toString())) {
            throw new RuntimeException("Unauthorized");
        }
        String oldStatus = order.getStatus().value();
        final var updateInfo = Order.UpdateInfo.builder().id(command.id()).shippingInfo(command.shippingInfo().apply(order.getShippingInfo())).orderStatus(command.status().apply(order.getStatus())).build();
        final var next = order.applyUpdateInfo(updateInfo);
        final var saved = saveOrderPort.save(next);
        boolean isSendEvent = !oldStatus.equals(saved.getStatus().value());
        final var listEvent = saved.getDetails().stream().map(item->new OrderDetail(item.variantId(),item.amount())).toList();
        if(saved.getStatus().value().equals("CANCELLED")&&isSendEvent) {
            CodPaymentCancelledDocument outboxEvent = CodPaymentCancelledDocument.builder()
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
            publishEventPort.publishOrderCancelled_InventoryEvent(new OrderCancelled(saved.getId().value(),listEvent,oldStatus));
        }
        if(saved.getStatus().value().equals("RECEIVED")&&isSendEvent) {
            publishEventPort.publishCodPaymentReceived(new CodPaymentReceived(saved.getId().value()));
        }
        if(saved.getStatus().value().equals("SHIPPING")&&isSendEvent) {
            publishEventPort.publishOrderShipped_InventoryEvent(new OrderShipped(saved.getId().value(),listEvent));
        }
        publishEventPort.publish(new OrderUpdated(saved.getId()));
    }

    @Override
    public void codOrderSuccess(OrderId orderId, String userIdFromHeader) {

        Order order = loadOrderPort.loadById(orderId).orElseThrow(()->new OrderNotFoundException(orderId));
        if(!checkPermission.isSameUser(userIdFromHeader, order.getUserId().value().toString())) {
            throw new RuntimeException("Unauthorized");
        }
        final var updateInfo = Order.UpdateInfo.builder().id(order.getId()).shippingInfo(order.getShippingInfo()).orderStatus(new OrderStatus("RECEIVED")).build();
        final var saved = saveOrderPort.save(order.applyUpdateInfo(updateInfo));
        publishEventPort.publishCodPaymentReceived(new CodPaymentReceived(order.getId().value()));
    }

    @Override
    public void codOrderCancelled(OrderId orderId) {
       Order order = loadOrderPort.loadById(orderId).orElseThrow(()->new OrderNotFoundException(orderId));
       String oldStatus = order.getStatus().value();
        final var updateInfo = Order.UpdateInfo.builder().id(order.getId()).shippingInfo(order.getShippingInfo()).orderStatus(new OrderStatus("CANCELLED")).build();
        final var saved = saveOrderPort.save(order.applyUpdateInfo(updateInfo));
        final var listEvent = saved.getDetails().stream().map(item->new OrderDetail(item.variantId(),item.amount())).toList();
        CodPaymentCancelledDocument outboxEvent = CodPaymentCancelledDocument.builder()
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
        publishEventPort.publishOrderCancelled_InventoryEvent(new OrderCancelled(saved.getId().value(),listEvent,oldStatus));
    }

    @Override
    public void forceCancellOrder(OrderId orderId) {
        Order order = loadOrderPort.loadById(orderId).orElseThrow(()->new OrderNotFoundException(orderId));
        final var updateInfo = Order.UpdateInfo.builder().id(order.getId()).shippingInfo(order.getShippingInfo()).orderStatus(new OrderStatus("CANCELLED")).build();
        saveOrderPort.save(order.applyUpdateInfo(updateInfo));
    }

}
