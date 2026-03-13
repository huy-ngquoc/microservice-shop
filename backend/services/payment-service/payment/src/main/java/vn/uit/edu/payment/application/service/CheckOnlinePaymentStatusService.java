package vn.uit.edu.payment.application.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.payos.PayOS;
import vn.payos.type.PaymentLinkData;
import vn.uit.edu.payment.application.port.in.LoadPaymentUseCase;
import vn.uit.edu.payment.application.port.out.CheckOnlinePaymentStatusPort;
import vn.uit.edu.payment.application.port.out.LoadOnlinePaymentInfoPort;
import vn.uit.edu.payment.application.port.out.PublishPaymentEventPort;
import vn.uit.edu.payment.application.port.out.SavePaymentPort;
import vn.uit.edu.payment.domain.event.OnlinePaymentExpired;
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
    

    @Override
    @Scheduled(fixedRate=15*60*1000)
    @Transactional
    public void checkOnlinePaymentStatus() {

        List<Payment> payments = loadUseCase.loadExpiredPayment(Instant.now().plus(15, ChronoUnit.MINUTES));
        List<OnlinePaymentInfo> onlinePaymentInfos = loadOnlinePaymentInfoPort.loadByPayments(payments);
        List<Payment> savedPayment = new ArrayList<>();
        for(OnlinePaymentInfo i:onlinePaymentInfos) {
            try {
                PaymentLinkData info = payOS.getPaymentLinkInformation(i.getPaymentNumber().value());
                Payment p = findPaymentInList(i.getPaymentId(), payments);
                if(p==null) {
                    throw new Exception("Fail");
                }
                if ("PAID".equals(info.getStatus())) {
                    p.setPaymentStatus(new PaymentStatus("SUCCESS"));
                } else {
                    p.setPaymentStatus(new PaymentStatus("EXPIRED"));
                    publishEventPort.publishPaymentExpired(new OnlinePaymentExpired(p.getOrderId().value()));
                }
                savedPayment.add(p);
                savePort.saveAll(savedPayment);
            }

            catch(Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        
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
