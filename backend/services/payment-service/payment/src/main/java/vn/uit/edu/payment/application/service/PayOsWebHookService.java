package vn.uit.edu.payment.application.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.payos.PayOS;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;
import vn.uit.edu.payment.application.exception.PaymentNotFoundException;
import vn.uit.edu.payment.application.port.out.LoadOnlinePaymentInfoPort;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.application.port.out.PayOsWebHookPort;
import vn.uit.edu.payment.application.port.out.SaveOnlinePaymentInfoPort;
import vn.uit.edu.payment.application.port.out.SavePaymentPort;
import vn.uit.edu.payment.domain.model.OnlinePaymentInfo;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.OnlinePaymentNumber;
import vn.uit.edu.payment.domain.model.valueobject.PaymentStatus;
import vn.uit.edu.payment.domain.model.valueobject.TransactionId;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayOsWebHookService implements PayOsWebHookPort {
    private final PayOS payOS;
    private final SaveOnlinePaymentInfoPort saveOnlinePaymentInfoPort;
    private final LoadOnlinePaymentInfoPort loadOnlinePaymentInfoPort;
    private final SavePaymentPort savePaymentPort;
    private final LoadPaymentPort loadPaymentPort;
    @Override
    @Transactional
    public void handlePayOSWebHook(Webhook body) {
        try {
            WebhookData webhookData = payOS.verifyPaymentWebhookData(body);
            long orderCode = webhookData.getOrderCode();
            OnlinePaymentInfo onlinePaymentInfo = loadOnlinePaymentInfoPort.loadByOrderCode(new OnlinePaymentNumber(orderCode));
            Payment payment = loadPaymentPort.loadPaymentById(onlinePaymentInfo.getPaymentId()).orElseThrow(()->new PaymentNotFoundException(onlinePaymentInfo.getPaymentId()));
            Payment.UpdateInfo updateInfo = Payment.UpdateInfo.builder().paymentId(payment.getPaymentId()).currency(payment.getCurrency()).paymentMethod(payment.getPaymentMethod()).paymentStatus(new PaymentStatus("PAID")).build();
            Payment saved = payment.applyUpdateInfo(updateInfo);
            savePaymentPort.save(saved);
            onlinePaymentInfo.setTransactionId(new TransactionId(""));
            saveOnlinePaymentInfoPort.save(onlinePaymentInfo);

        } catch (Exception ex) {
            System.getLogger(PayOsWebHookService.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

}
