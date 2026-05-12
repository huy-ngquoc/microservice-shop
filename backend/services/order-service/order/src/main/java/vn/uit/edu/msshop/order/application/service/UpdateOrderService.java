package vn.uit.edu.msshop.order.application.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;
import vn.uit.edu.msshop.order.adapter.in.web.request.OrderDetailRequest;
import vn.uit.edu.msshop.order.adapter.in.web.request.UpdateInventoryFromOrderServiceRequest;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderUpdatedEventDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderUpdatedRepository;
import vn.uit.edu.msshop.order.adapter.out.persistence.VariantSoldCountDocument;
import vn.uit.edu.msshop.order.adapter.out.persistence.VariantSoldCountRepository;
import vn.uit.edu.msshop.order.adapter.remote.InventoryChecker;
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
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderDetail;
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

    private final OrderUpdatedRepository orderUpdatedRepo;
    private final InventoryChecker inventoryChecker;
    private final VariantSoldCountRepository variantSoldCountRepo;
    /*
     * private UUID eventId;
     * private UUID orderId;
     * 
     * private List<OrderDetailEvent> details;
     * 
     * private String status;
     * 
     * private UUID userId;
     * 
     * private long originPrice;
     * 
     * private long shippingFee;
     * 
     * private long discount;
     * 
     * private long totalPrice;
     * private String currency;
     * private String paymentMethod;
     * private String paymentStatus;
     * private String email;
     */

    @Override
    @Transactional
    public void update(
            UpdateOrderCommand command,
            String userIdFromHeader,
            String role) {

        Order order = loadOrderPort.loadById(command.id()).orElseThrow(() -> new OrderNotFoundException(command.id()));
        if (!checkPermission.isAdmin(role)
                && !checkPermission.isSameUser(userIdFromHeader, order.getUserId().value().toString())) {
            throw new RuntimeException("Unauthorized");
        }
        System.out.println("Order currency is " + order.getCurrency().value());
        String oldStatus = order.getStatus().value();

        String commandStatus = command.status().apply(order.getStatus()).value();
        if (!oldStatus.equals(commandStatus)) {
            if (!preUpdateCheck(order, new OrderStatus(commandStatus), role)) {
                throw new WrongUpdateInfoException("Invalid order status");
            }
        }

        final var updateInfo = Order.UpdateInfo.builder().id(command.id())
                .shippingInfo(command.shippingInfo().apply(order.getShippingInfo()))
                .orderStatus(command.status().apply(order.getStatus()))
                .build();
        var next = order.applyUpdateInfo(updateInfo);
        if (next.getStatus().value().equals("CANCELLED")) {
            next = next.updatePaymentStatus(new PaymentStatus("CANCELLED"));
        }
        List<OrderDetailRequest> detailRequests = next.getDetails().stream()
                .map(item -> new OrderDetailRequest(item.variantId(), item.amount())).toList();
        UpdateInventoryFromOrderServiceRequest request = new UpdateInventoryFromOrderServiceRequest(
                next.getStatus().value(), oldStatus, detailRequests, next.getId().value());
        // inventoryChecker.updateFromOrderService(request);
        final var saved = saveOrderPort.save(next);
        boolean isSendEvent = !oldStatus.equals(saved.getStatus().value());
        if (isSendEvent && next.getStatus().value().equals("RECEIVED")) {
            updateVariantSoldCount(next);
        }
        if (isSendEvent) {
            System.out.println("Send eventtttttttttttttttttttttttttttttttt");
            List<OrderDetailEvent> detailEvents = saved.getDetails().stream()
                    .map(item -> new OrderDetailEvent(item.variantId(), item.productId(), item.amount())).toList();
            OrderUpdatedEventDocument eventDocument = OrderUpdatedEventDocument.builder()
                    .eventId(UUIDs.newId())
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
    public void forceCancellOrder(
            OrderId orderId) {
        Order order = loadOrderPort.loadById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        final var updateInfo = Order.UpdateInfo.builder().id(order.getId()).shippingInfo(order.getShippingInfo())
                .orderStatus(new OrderStatus("CANCELLED")).build();
        saveOrderPort.save(order.applyUpdateInfo(updateInfo));
    }

    private boolean preUpdateCheck(
            Order order,
            OrderStatus newStatus,
            String role) {

        String newStatusValue = newStatus.getValue();
        String oldStatusValue = order.getStatus().value();
        String paymentStatus = order.getPaymentStatus().value();
        String paymentMethod = order.getPaymentMethod().value();
        if (newStatusValue.equals("PENDING") || newStatusValue.equals("CONFIRMED"))
            return false;
        if (newStatusValue.equals("SHIPPING")) {
            if (!checkPermission.isAdmin(role))
                return false;
            if (!oldStatusValue.equals("CONFIRMED"))
                return false;
            if (paymentMethod.equals("ONLINE") && !paymentStatus.equals("SUCCESS"))
                return false;
        }
        if (newStatusValue.equals("CANCELLED")) {
            if (!oldStatusValue.equals("PENDING") && !oldStatusValue.equals("SHIPPING")
                    && !oldStatusValue.equals("CONFIRMED") && !oldStatusValue.equals("WAITING_PAYMENT")
                    && !oldStatusValue.equals("PENDING_PAYMENT"))
                return false;
        }
        if (newStatusValue.equals("RECEIVED")) {
            if (!oldStatusValue.equals("SHIPPING"))
                return false;
        }
        return true;
    }

    private void updateVariantSoldCount(
            Order order) {
        List<VariantSoldCountDocument> toSavesVariantSoldCount = new ArrayList<>();
        List<VariantSoldCountDocument> variantSoldCountFromRepo = variantSoldCountRepo
                .findByIdIn(order.getDetails().stream().map(item -> item.variantId()).toList());
        Map<UUID, VariantSoldCountDocument> soldCountMap = new HashMap<>();
        for (VariantSoldCountDocument vSD : variantSoldCountFromRepo) {
            soldCountMap.put(vSD.getId(), vSD);
        }
        for (OrderDetail oD : order.getDetails()) {
            VariantSoldCountDocument vsD = soldCountMap.get(oD.variantId());
            if (vsD == null) {
                toSavesVariantSoldCount.add(new VariantSoldCountDocument(oD.variantId(), oD.amount(), null));
                continue;
            }
            vsD.setSoldCount(vsD.getSoldCount() + oD.amount());
            toSavesVariantSoldCount.add(vsD);
        }
        variantSoldCountRepo.saveAll(toSavesVariantSoldCount);
    }

}
