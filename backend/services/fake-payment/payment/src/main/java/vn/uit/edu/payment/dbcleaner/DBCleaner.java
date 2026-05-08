package vn.uit.edu.payment.dbcleaner;

import org.springframework.stereotype.Component;

import vn.uit.edu.payment.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.payment.adapter.out.event.repositories.OnlinePaymentExpiredDocumentRepository;
import vn.uit.edu.payment.adapter.out.event.repositories.PaymentSuccessDocumentRepository;
import vn.uit.edu.payment.adapter.out.persistence.SpringDataOnlinePaymentInfoJpaRepository;
import vn.uit.edu.payment.adapter.out.persistence.SpringDataPaymentJpaRepository;

//@Component
public class DBCleaner {
    
    private  OnlinePaymentExpiredDocumentRepository onlinePaymentExpiredRepo;
   
    private  PaymentSuccessDocumentRepository paymentSuccessRepo;
    private EventDocumentRepository eventDocumentRepo;
    private SpringDataPaymentJpaRepository paymentRepo;
    private SpringDataOnlinePaymentInfoJpaRepository onlinePaymentRepo;

    public DBCleaner( EventDocumentRepository eventDocumentRepo, 
         OnlinePaymentExpiredDocumentRepository onlinePaymentExpiredRepo, PaymentSuccessDocumentRepository paymentSuccessRepo, SpringDataPaymentJpaRepository paymentRepo, SpringDataOnlinePaymentInfoJpaRepository onlinePaymentRepo) {
       
        this.eventDocumentRepo = eventDocumentRepo;
        
        this.onlinePaymentExpiredRepo = onlinePaymentExpiredRepo;
        this.paymentSuccessRepo = paymentSuccessRepo;
        this.onlinePaymentRepo=onlinePaymentRepo;
        this.paymentRepo=paymentRepo;
        
        eventDocumentRepo.deleteAll();
        
        onlinePaymentExpiredRepo.deleteAll();
        paymentSuccessRepo.deleteAll();
        onlinePaymentRepo.deleteAll();
        paymentRepo.deleteAll();
    }
    
}
