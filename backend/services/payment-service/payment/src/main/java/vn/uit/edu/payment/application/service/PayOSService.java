package vn.uit.edu.payment.application.service;

import java.time.Instant;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.PaymentLink;
import vn.uit.edu.payment.adapter.out.event.documents.OnlinePaymentExpiredDocument;
import vn.uit.edu.payment.adapter.out.event.documents.PaymentSuccessDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.OnlinePaymentExpiredDocumentRepository;
import vn.uit.edu.payment.adapter.out.event.repositories.PaymentSuccessDocumentRepository;
import vn.uit.edu.payment.application.port.in.PayOSUseCase;
import vn.uit.edu.payment.application.port.out.LoadOnlinePaymentInfoPort;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.application.port.out.PublishPaymentEventPort;
import vn.uit.edu.payment.application.port.out.SavePaymentPort;
import vn.uit.edu.payment.bootstrap.config.cache.CacheNames;
import vn.uit.edu.payment.domain.model.OnlinePaymentInfo;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.OnlinePaymentNumber;
import vn.uit.edu.payment.domain.model.valueobject.PaymentStatus;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayOSService implements PayOSUseCase {
    private final PayOS payOS;

    private final LoadOnlinePaymentInfoPort loadOnlinePaymentInfoPort;

    private final SavePaymentPort savePaymentPort;
    private final LoadPaymentPort loadPaymentPort;
    private final PaymentSuccessDocumentRepository paymentSuccessRepo;

    private final PublishPaymentEventPort eventPort;
    private final OnlinePaymentExpiredDocumentRepository onlinePaymentExpiredRepo;

    // TODO: refactor this method to refactor `@Caching` more effective.
    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.PAYMENT_BY_ID,
                            allEntries = true),
                    @CacheEvict(
                            cacheNames = CacheNames.PAYMENT_BY_ORDER_ID,
                            allEntries = true),
                    @CacheEvict(
                            cacheNames = CacheNames.ONLINE_PAYMENT_LINK_BY_ORDER_ID,
                            allEntries = true)
            })
    public void syncPaymentData(
            long orderCode) {
        try {
            OnlinePaymentInfo onlinePaymentInfo = loadOnlinePaymentInfoPort
                    .loadByOrderCode(new OnlinePaymentNumber(orderCode));
            if (onlinePaymentInfo == null) {
                log.info("Online payment info is null");
                return;
            }
            Payment payment = loadPaymentPort.loadPaymentById(onlinePaymentInfo.getPaymentId()).orElse(null);
            if (payment == null) {
                log.info("Payment is null");
                return;
            }

            PaymentLink paymentInfo = payOS.paymentRequests().get(orderCode);

            String status = paymentInfo.getStatus().getValue();
            System.out.println(status); // "PAID", "PENDING", "CANCELLED", "EXPIRED"

            log.info("Trạng thái đơn hàng {} trên PayOS là: {}", orderCode, status);

            if ("PAID".equals(status)) {
                handlePaymentSuccessLogic(payment);

            } else if ("CANCELLED".equals(status) || "EXPIRED".equals(status)) {
                if ("PENDING".equals(payment.getPaymentStatus().value())) {
                    handleOnlinePaymentExpired(payment);
                }
            }
        } catch (Exception e) {
            log.error("Không thể lấy thông tin từ PayOS cho đơn {}: {}", orderCode, e.getMessage());
        }
    }

    private void handlePaymentSuccessLogic(
            Payment payment) {

        Payment.UpdateInfo updateInfo = Payment.UpdateInfo.builder()
                .paymentId(payment.getPaymentId())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(new PaymentStatus("SUCCESS"))
                .build();

        Payment saved = savePaymentPort.save(payment.applyUpdateInfo(updateInfo));

        // Tạo các tài liệu sự kiện (Outbox Pattern)
        PaymentSuccessDocument paymentEvent = createPaymentSuccessEvent(saved);

        final var savedPaymentSuccessEvent = paymentSuccessRepo.save(paymentEvent);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {

                eventPort.publishPaymentSuccess(savedPaymentSuccessEvent);
                log.info("Đã publish thành công các event thanh toán cho đơn: {}", saved.getOrderId().value());
            }
        });
    }

    private PaymentSuccessDocument createPaymentSuccessEvent(
            Payment p) {
        return PaymentSuccessDocument.builder()
                .eventId(UUIDs.newId())
                .orderId(p.getOrderId().value())
                .eventStatus("PENDING").retryCount(0)
                .createdAt(Instant.now()).build();
    }

    private void handleOnlinePaymentExpired(
            Payment payment) {

        if (payment != null) {
            final var updateInfo = Payment.UpdateInfo.builder().paymentId(payment.getPaymentId())
                    .currency(payment.getCurrency())
                    .paymentStatus(new PaymentStatus("CANCELLED")).paymentMethod(payment.getPaymentMethod()).build();
            final var saved = payment.applyUpdateInfo(updateInfo);
            savePaymentPort.save(saved);
            final var savedEvent = onlinePaymentExpiredRepo.save(createOnlinePaymentExpiredEvent(saved));

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {

                    eventPort.publishPaymentExpired(savedEvent);
                }
            });
        }
    }

    private OnlinePaymentExpiredDocument createOnlinePaymentExpiredEvent(
            Payment p) {
        /*
         * private UUID eventId;
         * private UUID orderId;
         * private String eventStatus;
         * private UUID userId;
         * private Integer retryCount;
         * private Instant createdAt;
         * private Instant updatedAt;
         * private String lastError;
         * private String userEmail;
         */
        return OnlinePaymentExpiredDocument.builder().eventId(UUIDs.newId())
                .orderId(p.getOrderId().value())
                .eventStatus("PENDING")
                .userId(p.getUserId().value())
                .retryCount(0)
                .createdAt(Instant.now())
                .updatedAt(null)
                .lastError(null)
                .userEmail(p.getUserEmail().value())
                .build();
    }

}
