package vn.uit.edu.payment.application.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.payos.PayOS;
import vn.uit.edu.payment.adapter.out.event.documents.OnlinePaymentExpiredDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.OnlinePaymentExpiredDocumentRepository;
import vn.uit.edu.payment.adapter.out.event.repositories.PaymentSuccessDocumentRepository;
import vn.uit.edu.payment.application.port.in.PayOSUseCase;
import vn.uit.edu.payment.application.port.out.LoadOnlinePaymentInfoPort;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.application.port.out.PublishPaymentEventPort;
import vn.uit.edu.payment.application.port.out.SavePaymentPort;
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

    @Override
    @Transactional
    public void syncPaymentData(long orderCode) {
        
        OnlinePaymentInfo  onlinePaymentInfo = loadOnlinePaymentInfoPort.loadByOrderCode(new OnlinePaymentNumber(orderCode));
        if(onlinePaymentInfo==null) {
            log.info("Online payment info is null");
            return;
        }
        Payment payment = loadPaymentPort.loadPaymentById(onlinePaymentInfo.getPaymentId()).orElse(null);
        if(payment==null) {
            log.info("Payment is null");
            return;
        }
        if(payment.getPaymentStatus().value().equals("PENDING")) {
            handleOnlinePaymentExpired(payment);
        }
        

       
        
    }
private void handleOnlinePaymentExpired(Payment payment) {
       
        if(payment!=null) {
            final var updateInfo = Payment.UpdateInfo.builder().paymentId(payment.getPaymentId()).currency(payment.getCurrency())
            .paymentStatus(new PaymentStatus("EXPIRED")).paymentMethod(payment.getPaymentMethod()).build();
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
    private OnlinePaymentExpiredDocument createOnlinePaymentExpiredEvent(Payment p) {
        /*private UUID eventId;
    private UUID orderId;
    private String eventStatus;
    private UUID userId;
    private Integer retryCount; 
    private Instant createdAt;
    private Instant updatedAt; 
    private String lastError;
    private String userEmail; */
        return OnlinePaymentExpiredDocument.builder().eventId(UUID.randomUUID())
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

