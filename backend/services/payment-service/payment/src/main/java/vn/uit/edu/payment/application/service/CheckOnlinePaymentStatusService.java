package vn.uit.edu.payment.application.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import vn.payos.PayOS;
import vn.uit.edu.payment.adapter.out.event.documents.OnlinePaymentExpiredDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.OnlinePaymentExpiredDocumentRepository;
import vn.uit.edu.payment.application.port.in.LoadPaymentUseCase;
import vn.uit.edu.payment.application.port.out.CheckOnlinePaymentStatusPort;
import vn.uit.edu.payment.application.port.out.LoadOnlinePaymentInfoPort;
import vn.uit.edu.payment.application.port.out.PublishPaymentEventPort;
import vn.uit.edu.payment.application.port.out.SavePaymentPort;
import vn.uit.edu.payment.domain.model.OnlinePaymentInfo;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentStatus;
@Component
@RequiredArgsConstructor
public class CheckOnlinePaymentStatusService implements CheckOnlinePaymentStatusPort {
    private final LoadPaymentUseCase loadUseCase;
    private final LoadOnlinePaymentInfoPort loadOnlinePaymentInfoPort;
    private final PublishPaymentEventPort publishEventPort;
    private final SavePaymentPort savePort;
    private final PayOS payOS;
    private final OnlinePaymentExpiredDocumentRepository onlinePaymentExpiredDocumentRepo;
    

    @Override
    @Scheduled(fixedRate=15*60*1000)
    @Transactional
    public void checkOnlinePaymentStatus() {

        List<Payment> payments = loadUseCase.loadExpiredPayment(Instant.now().plus(15, ChronoUnit.MINUTES));
        List<OnlinePaymentInfo> onlinePaymentInfos = loadOnlinePaymentInfoPort.loadByPayments(payments);
        List<Payment> savedPayment = new ArrayList<>();
        for(OnlinePaymentInfo i:onlinePaymentInfos) {
            
                Payment p = findPaymentInList(i.getPaymentId(), payments);
                p.setPaymentStatus(new PaymentStatus("EXPIRED"));

                OnlinePaymentExpiredDocument outboxEvent = OnlinePaymentExpiredDocument.builder()
                .eventId(UUID.randomUUID())
        .orderId(p.getOrderId().value())
        .userEmail(p.getUserEmail().value())
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null).build();
        final var savedOutboxEvent = onlinePaymentExpiredDocumentRepo.save(outboxEvent);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishEventPort.publishPaymentExpired(savedOutboxEvent);
            }
        });

                //publishEventPort.publishPaymentExpired(new OnlinePaymentExpired(p.getOrderId().value()));
                savedPayment.add(p);
                
            
        }
        savePort.saveAll(savedPayment);
        
    }
    private Payment findPaymentInList(PaymentId paymentId, List<Payment> payments) {
        for(Payment p: payments) {
            if(p.getPaymentId().value().equals(paymentId.value())) {
                return p;
            }
        }
        return null;
    }

}
