package vn.uit.edu.payment.application.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.application.port.in.PayOSWorker;
import vn.uit.edu.payment.application.port.out.LoadOnlinePaymentInfoPort;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.domain.model.OnlinePaymentInfo;
import vn.uit.edu.payment.domain.model.Payment;

@Service
@RequiredArgsConstructor
public class PayOSWorkerAdapter implements PayOSWorker {
    private final LoadPaymentPort loadPaymentPort;
    private final LoadOnlinePaymentInfoPort loadPaymentInfoPort;
    private final PayOSService payOSService;

    @Override
    @Scheduled(fixedDelay=60000*5)
    public void checkExpiredPayments() {
        Instant timeOut = Instant.now().minus(1, ChronoUnit.MINUTES);
        List<Payment> payments = loadPaymentPort.loadExpiredPayment(timeOut);
        for(Payment payment:payments) {
            OnlinePaymentInfo onlinePaymentInfo = loadPaymentInfoPort.loadById(payment.getPaymentId());
            if(onlinePaymentInfo==null) continue;
            payOSService.syncPaymentData(onlinePaymentInfo.getPaymentNumber().value());

        }
    }

}
