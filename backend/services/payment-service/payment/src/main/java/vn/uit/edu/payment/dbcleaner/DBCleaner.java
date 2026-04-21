package vn.uit.edu.payment.dbcleaner;

import org.springframework.stereotype.Component;

import vn.uit.edu.payment.adapter.out.event.repositories.CodPaymentCreatedDocumentRepository;
import vn.uit.edu.payment.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.payment.adapter.out.event.repositories.OnlinePaymentCancelledDocumentRepository;
import vn.uit.edu.payment.adapter.out.event.repositories.OnlinePaymentExpiredDocumentRepository;
import vn.uit.edu.payment.adapter.out.event.repositories.PaymentSuccessDocumentRepository;
import vn.uit.edu.payment.adapter.out.persistence.SpringDataOnlinePaymentInfoJpaRepository;
import vn.uit.edu.payment.adapter.out.persistence.SpringDataPaymentJpaRepository;

@Component
public class DBCleaner {
    private  OnlinePaymentCancelledDocumentRepository onlinePaymentCancelledRepo;
    private  OnlinePaymentExpiredDocumentRepository onlinePaymentExpiredRepo;
    private  CodPaymentCreatedDocumentRepository codPaymentCreatedRepo;
    private  PaymentSuccessDocumentRepository paymentSuccessRepo;
    private EventDocumentRepository eventDocumentRepo;
    private SpringDataPaymentJpaRepository paymentRepo;
    private SpringDataOnlinePaymentInfoJpaRepository onlinePaymentRepo;

    public DBCleaner(CodPaymentCreatedDocumentRepository codPaymentCreatedRepo, EventDocumentRepository eventDocumentRepo, OnlinePaymentCancelledDocumentRepository onlinePaymentCancelledRepo,
         OnlinePaymentExpiredDocumentRepository onlinePaymentExpiredRepo, PaymentSuccessDocumentRepository paymentSuccessRepo, SpringDataPaymentJpaRepository paymentRepo, SpringDataOnlinePaymentInfoJpaRepository onlinePaymentRepo) {
        this.codPaymentCreatedRepo = codPaymentCreatedRepo;
        this.eventDocumentRepo = eventDocumentRepo;
        this.onlinePaymentCancelledRepo = onlinePaymentCancelledRepo;
        this.onlinePaymentExpiredRepo = onlinePaymentExpiredRepo;
        this.paymentSuccessRepo = paymentSuccessRepo;
        this.onlinePaymentRepo=onlinePaymentRepo;
        this.paymentRepo=paymentRepo;
        codPaymentCreatedRepo.deleteAll();
        eventDocumentRepo.deleteAll();
        onlinePaymentCancelledRepo.deleteAll();
        onlinePaymentExpiredRepo.deleteAll();
        paymentSuccessRepo.deleteAll();
        onlinePaymentRepo.deleteAll();
        paymentRepo.deleteAll();
    }
    
}
