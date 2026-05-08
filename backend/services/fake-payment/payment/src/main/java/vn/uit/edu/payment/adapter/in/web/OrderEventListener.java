package vn.uit.edu.payment.adapter.in.web;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.payment.adapter.in.web.mapper.PaymentWebMapper;
import vn.uit.edu.payment.adapter.out.event.documents.EventDocument;
import vn.uit.edu.payment.adapter.out.event.documents.PaymentCreatedFailDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.payment.adapter.out.event.repositories.PaymentCreatedFailedRepository;
import vn.uit.edu.payment.adapter.out.persistence.PaybackPaymentRepository;
import vn.uit.edu.payment.adapter.out.persistence.PaybackPayments;
import vn.uit.edu.payment.application.dto.command.CreatePaymentCommand;
import vn.uit.edu.payment.application.port.in.CreatePaymentUseCase;
import vn.uit.edu.payment.application.port.out.CancellPaymentLinkPort;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.application.port.out.PublishPaymentEventPort;
import vn.uit.edu.payment.application.port.out.SavePaymentPort;
import vn.uit.edu.payment.domain.event.OrderCreated;
import vn.uit.edu.payment.domain.event.OrderUpdatedEvent;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentStatus;

@Component
@Slf4j
@RequiredArgsConstructor
@RetryableTopic(
        attempts = "4",
        include = { RuntimeException.class })
@KafkaListener(
        topics = "order-topic",
        groupId = "order-payment-group")
public class OrderEventListener {
    private final PaymentWebMapper mapper;
    private final CreatePaymentUseCase createUseCase;
    private final EventDocumentRepository eventDocumentRepo;
    private final PaybackPaymentRepository paybackPaymentRepo;
    private final LoadPaymentPort loadPort;
    private final SavePaymentPort savePort;
    private final CancellPaymentLinkPort cancelPaymentLinkPort;
    private final PaymentCreatedFailedRepository paymentCreatedFailRepo;
    private final PublishPaymentEventPort publishEventPort;
    private final PaymentCreatedFailService paymentCreatedFailService;

    // @KafkaHandler

    public void handleOrderCreated(
            OrderCreated event) {
        Random rand = new Random();
        if (rand.nextLong(200) <= 20)
            throw new RuntimeException("Runtime exception");
        // System.out.println("Listen to event");
        if (!eventDocumentRepo.existsById(event.eventId())) {
            CreatePaymentCommand command = mapper.toCommand(event);
            createUseCase.create(command);
            eventDocumentRepo.save(new EventDocument(event.eventId(), Instant.now()));
        }
    }

    @DltHandler
    public void paymentCreatedFail(
            OrderCreated event) {
        PaymentCreatedFailDocument document = PaymentCreatedFailDocument.builder()
                .eventId(UUID.randomUUID())
                .orderId(event.orderId())
                .userId(event.userId())
                .userEmail(event.userEmail())
                .retryCount(0)
                .createdAt(Instant.now())
                .updatedAt(null)
                .lastError(null).build();
        paymentCreatedFailService.saveAndSendPaymentCreatedFail(document);

    }

    @KafkaHandler
    @Transactional
    public void handleOrderUpdated(
            OrderUpdatedEvent event) {
        if (!eventDocumentRepo.existsById(event.getEventId())) {

            if (event.getPaymentMethod().equals("COD")) {
                if (event.getStatus().equals("CANCELLED")) {
                    handleCodOrderCancelled(event.getOrderId());
                }
                if (event.getStatus().equals("RECEIVED")) {
                    handleCodOrderReceived(event.getOrderId());
                }
            } else {

                if (event.getPaymentStatus().equals("UNPAID") && event.getStatus().equals("CANCELLED")) {
                    handleOnlinePaymentCancelled(event.getOrderId());
                }
                if (event.getPaymentStatus().equals("PAID") && event.getStatus().equals("CANCELLED")) {
                    Payment p = loadPort.loadPaymentByOrderId(new OrderId(event.getOrderId()));
                    if (p == null)
                        return;
                    PaybackPayments newPaybackPayment = PaybackPayments.builder().userId(event.getUserId())
                            .value(p.getPaymentValue().value()).build();
                    paybackPaymentRepo.save(newPaybackPayment);
                }
                if (event.getStatus().equals("PAYMENT_EXPIRED")) {
                    System.out.println("Link het han");
                    handleOnlinePaymentCancelled(event.getOrderId());
                }
            }
        }
    }

    @KafkaHandler(
            isDefault = true)
    public void onObjectReceived(
            Object event) {
        System.out.println("Nhan event la");

    }

    private void handleOnlinePaymentCancelled(
            UUID orderId) {
        Payment payment = loadPort.loadPaymentByOrderId(new OrderId(orderId));
        if (payment != null) {
            final var updateInfo = Payment.UpdateInfo.builder().paymentId(payment.getPaymentId())
                    .currency(payment.getCurrency())
                    .paymentStatus(new PaymentStatus("CANCELLED")).paymentMethod(payment.getPaymentMethod()).build();
            final var saved = payment.applyUpdateInfo(updateInfo);
            savePort.save(saved);
            cancelPaymentLinkPort.cancelPaymentLink(saved.getOrderId());
        }
    }

    private void handleCodOrderCancelled(
            UUID orderId) {
        Payment payment = loadPort.loadPaymentByOrderId(new OrderId(orderId));
        if (payment != null) {
            final var updateInfo = Payment.UpdateInfo.builder().paymentId(payment.getPaymentId())
                    .currency(payment.getCurrency())
                    .paymentStatus(new PaymentStatus("CANCELLED")).paymentMethod(payment.getPaymentMethod()).build();
            final var saved = payment.applyUpdateInfo(updateInfo);
            savePort.save(saved);
        }
    }

    private void handleCodOrderReceived(
            UUID orderId) {
        Payment payment = loadPort.loadPaymentByOrderId(new OrderId(orderId));
        if (payment != null) {
            final var updateInfo = Payment.UpdateInfo.builder().paymentId(payment.getPaymentId())
                    .currency(payment.getCurrency())
                    .paymentStatus(new PaymentStatus("SUCCESS")).paymentMethod(payment.getPaymentMethod()).build();
            final var saved = payment.applyUpdateInfo(updateInfo);
            savePort.save(saved);
        }
    }

}
