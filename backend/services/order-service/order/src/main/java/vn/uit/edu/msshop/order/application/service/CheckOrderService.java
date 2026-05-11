package vn.uit.edu.msshop.order.application.service;

import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;
import vn.uit.edu.msshop.order.adapter.in.web.response.PaymentResponse;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderUpdatedEventDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderUpdatedRepository;
import vn.uit.edu.msshop.order.adapter.remote.PaymentChecker;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.domain.event.OrderDetailEvent;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.PaymentStatus;

@Service
@RequiredArgsConstructor
public class CheckOrderService {
    private final PaymentChecker paymentChecker;
    private final SaveOrderPort savePort;
    private final OrderUpdatedRepository orderUpdatedRepo;
    private final PublishOrderEventPort publishPort;

    @Transactional
    public void checkAndUpdate(
            Order order) {
        OrderUpdatedEventDocument eventDocument;
        eventDocument = null;
        try {
            PaymentResponse payment = paymentChecker.getPaymentByOrderId(order.getId().value()).getBody();
            if (!payment.paymentStatus().equals("SUCCESS")) {
                order = order.updatePaymentStatus(new PaymentStatus("EXPIRED"));
                String oldStatus = order.getStatus().value();
                order = order.updateStatus(new OrderStatus("PAYMENT_EXPIRED"));
                savePort.save(order);
                if (oldStatus.equals("PAYMENT_EXPIRED"))
                    return;
                eventDocument = orderUpdatedRepo.save(getOrderUpdatedEvent(order));
            } else {

            }
        } catch (FeignException e) {
            if (e.status() == 404) {
                order = order.updatePaymentStatus(new PaymentStatus("ERROR"));
                String oldStatus = order.getStatus().value();
                order = order.updateStatus(new OrderStatus("PAYMENT_ERROR"));
                savePort.save(order);
                if (oldStatus.equals("PAYMENT_ERROR"))
                    return;
                eventDocument = orderUpdatedRepo.save(getOrderUpdatedEvent(order));
            }

        }
        final var event = eventDocument;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                if (event == null)
                    return;
                publishPort.publishOrderUpdatedEvent(event);
            }
        });

    }

    private OrderUpdatedEventDocument getOrderUpdatedEvent(
            Order order) {
        List<OrderDetailEvent> detailEvents = order.getDetails().stream()
                .map(item -> new OrderDetailEvent(item.variantId(), item.productId(), item.amount())).toList();
        return OrderUpdatedEventDocument.builder()
                .eventId(UUIDs.newId())
                .orderId(order.getId().value())
                .details(detailEvents)
                .status(order.getStatus().value())
                .userId(order.getUserId().value())
                .originPrice(order.getOriginPrice().value())
                .shippingFee(order.getShippingFee().value())
                .discount(order.getDiscount().value())
                .totalPrice(order.getTotalPrice().value())
                .currency(order.getCurrency().value())
                .paymentMethod(order.getPaymentMethod().value())
                .paymentStatus(order.getPaymentStatus().value())
                .email(order.getShippingInfo().email())
                .oldStatus("PENDING")
                .eventStatus("PENDING")
                .retryCount(0)
                .createdAt(Instant.now())
                .updatedAt(null)
                .lastError(null).build();
    }
}
