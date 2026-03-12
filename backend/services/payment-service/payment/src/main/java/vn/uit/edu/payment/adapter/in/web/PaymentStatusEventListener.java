package vn.uit.edu.payment.adapter.in.web;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.application.port.out.SavePaymentPort;
import vn.uit.edu.payment.domain.event.CodPaymentCancelled;
import vn.uit.edu.payment.domain.event.CodPaymentReceived;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentStatus;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics="payment-cod-topic",groupId="order-payment-group")
public class PaymentStatusEventListener {
    private final LoadPaymentPort loadPort;
    private final SavePaymentPort savePort;
    @KafkaHandler
    public void handleCodOrderReceived(CodPaymentReceived event) {
        Payment payment = loadPort.loadPaymentByOrderId(new OrderId(event.orderId()));
        if(payment!=null) {
            final var updateInfo = Payment.UpdateInfo.builder().paymentId(payment.getPaymentId()).currency(payment.getCurrency())
            .paymentStatus(new PaymentStatus("SUCCESS")).paymentMethod(payment.getPaymentMethod()).build();
            final var saved = payment.applyUpdateInfo(updateInfo);
            savePort.save(saved);
        }
    }
    @KafkaHandler
    public void handleCodOrderCancelled(CodPaymentCancelled event) {
        Payment payment = loadPort.loadPaymentByOrderId(new OrderId(event.orderId()));
        if(payment!=null) {
            final var updateInfo = Payment.UpdateInfo.builder().paymentId(payment.getPaymentId()).currency(payment.getCurrency())
            .paymentStatus(new PaymentStatus("CANCELLED")).paymentMethod(payment.getPaymentMethod()).build();
            final var saved = payment.applyUpdateInfo(updateInfo);
            savePort.save(saved);
        }
    }
}
