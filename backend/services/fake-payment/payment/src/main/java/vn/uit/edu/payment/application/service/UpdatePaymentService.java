package vn.uit.edu.payment.application.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;
import vn.uit.edu.payment.adapter.out.event.documents.OnlinePaymentExpiredDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.OnlinePaymentExpiredDocumentRepository;
import vn.uit.edu.payment.application.dto.command.UpdatePaymentCommand;
import vn.uit.edu.payment.application.dto.query.PaymentView;
import vn.uit.edu.payment.application.exception.PaymentNotFoundException;
import vn.uit.edu.payment.application.mapper.PaymentViewMapper;
import vn.uit.edu.payment.application.port.in.UpdatePaymentUseCase;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.application.port.out.PublishPaymentEventPort;
import vn.uit.edu.payment.application.port.out.SavePaymentPort;
import vn.uit.edu.payment.domain.event.PaymentUpdated;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentStatus;

@Service
@RequiredArgsConstructor
public class UpdatePaymentService implements UpdatePaymentUseCase {
    private final LoadPaymentPort loadPort;
    private final SavePaymentPort savePort;
    private final PublishPaymentEventPort eventPort;
    private final PaymentViewMapper mapper;

    private final OnlinePaymentExpiredDocumentRepository onlinePaymentExpiredDocumentRepo;

    @Override
    @Transactional
    public PaymentView update(
            UpdatePaymentCommand command) {
        final var payment = loadPort.loadPaymentById(command.paymentId())
                .orElseThrow(() -> new PaymentNotFoundException(command.paymentId()));
        final var update = Payment.UpdateInfo.builder().paymentId(command.paymentId())
                .currency(command.currency().apply(payment.getCurrency()))
                .paymentMethod(command.paymentMethod().apply(payment.getPaymentMethod()))
                .paymentStatus(command.paymentStatus().apply(payment.getPaymentStatus()))
                .build();
        final var next = payment.applyUpdateInfo(update);
        final Payment saved = savePort.save(next);
        eventPort.publish(new PaymentUpdated(saved.getPaymentId()));
        return mapper.toView(saved);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void onlinePaymentExpire(
            OrderId orderId) {
        final var payment = loadPort.loadPaymentByOrderId(orderId);
        if (payment == null)
            return;
        final var update = Payment.UpdateInfo.builder().paymentId(payment.getPaymentId())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod()).paymentStatus(new PaymentStatus("EXPIRED"))
                .build();
        final var next = payment.applyUpdateInfo(update);
        final Payment saved = savePort.save(next);
        OnlinePaymentExpiredDocument outboxEvent = OnlinePaymentExpiredDocument.builder()
                .eventId(UUIDs.newId())
                .orderId(saved.getOrderId().value())
                .userId(saved.getUserId().value())
                .eventStatus("PENDING")

                .retryCount(0)
                .createdAt(Instant.now())
                .updatedAt(null)
                .lastError(null).build();
        final var savedOutboxEvent = onlinePaymentExpiredDocumentRepo.save(outboxEvent);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                eventPort.publishPaymentExpired(savedOutboxEvent);
            }
        });
        // eventPort.publishPaymentExpired(new OnlinePaymentExpired(orderId.value()));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void onlinePaymentCancelled(
            OrderId orderId) {
        final var payment = loadPort.loadPaymentByOrderId(orderId);
        if (payment == null)
            return;
        final var update = Payment.UpdateInfo.builder().paymentId(payment.getPaymentId())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod()).paymentStatus(new PaymentStatus("CANCELLED"))
                .build();
        final var next = payment.applyUpdateInfo(update);
        final Payment saved = savePort.save(next);
    }

}
