package vn.uit.edu.payment.adapter.in.web;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.adapter.out.event.documents.PaymentCreatedFailDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.PaymentCreatedFailedRepository;
import vn.uit.edu.payment.application.port.out.PublishPaymentEventPort;

@Service
@RequiredArgsConstructor
public class PaymentCreatedFailService {
    private final PaymentCreatedFailedRepository paymentCreatedFailRepo;
    private final PublishPaymentEventPort publishEventPort;
     @Transactional
    public void saveAndSendPaymentCreatedFail(PaymentCreatedFailDocument document) {
        final var savedEvent = paymentCreatedFailRepo.save(document);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishEventPort.publishPaymentCreatedFail(savedEvent);
            }
        });
    }
}
