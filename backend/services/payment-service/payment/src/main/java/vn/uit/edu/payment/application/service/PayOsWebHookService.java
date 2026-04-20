package vn.uit.edu.payment.application.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.payos.PayOS;
import vn.payos.model.webhooks.Webhook;
import vn.payos.model.webhooks.WebhookData;
import vn.uit.edu.payment.adapter.out.event.documents.OnlinePaymentSuccessDocument;
import vn.uit.edu.payment.adapter.out.event.documents.PaymentSuccessDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.OnlinePaymentSuccessRepository;
import vn.uit.edu.payment.adapter.out.event.repositories.PaymentSuccessDocumentRepository;
import vn.uit.edu.payment.application.exception.PaymentNotFoundException;
import vn.uit.edu.payment.application.port.out.LoadOnlinePaymentInfoPort;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.application.port.out.PayOsWebHookPort;
import vn.uit.edu.payment.application.port.out.PublishPaymentEventPort;
import vn.uit.edu.payment.application.port.out.SaveOnlinePaymentInfoPort;
import vn.uit.edu.payment.application.port.out.SavePaymentPort;
import vn.uit.edu.payment.domain.model.OnlinePaymentInfo;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.OnlinePaymentNumber;
import vn.uit.edu.payment.domain.model.valueobject.PaymentStatus;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayOsWebHookService implements PayOsWebHookPort {
    private final PayOS payOS;
    private final SaveOnlinePaymentInfoPort saveOnlinePaymentInfoPort;
    private final LoadOnlinePaymentInfoPort loadOnlinePaymentInfoPort;
    private final SavePaymentPort savePaymentPort;
    private final LoadPaymentPort loadPaymentPort;
    private final PublishPaymentEventPort eventPort;
    private final PaymentSuccessDocumentRepository paymentSuccessRepo;
    private final PublishPaymentEventPort publishEventPort;
    private final OnlinePaymentSuccessRepository onlinePaymentSuccessRepo;
    @Override

public void handlePayOSWebHook(Webhook body) {
    WebhookData webhookData;
    try {
        
        webhookData = payOS.webhooks().verify(body);
    } catch (Exception ex) {
        log.error("Xác thực Webhook thất bại! Có thể là request giả mạo.");
        return;
    }

    
    try {
        this.processPaymentUpdate(webhookData);
    } catch (Exception ex) {
        log.error("Lỗi cập nhật dữ liệu thanh toán: ", ex);
        
        throw ex; 
    }
}

@Transactional
public void processPaymentUpdate(WebhookData webhookData) {
    long orderCode = webhookData.getOrderCode();
    
    System.out.println("Order code "+orderCode);
    if(orderCode == 123) return;

    OnlinePaymentInfo onlinePaymentInfo = loadOnlinePaymentInfoPort.loadByOrderCode(new OnlinePaymentNumber(orderCode));
    
    Payment payment = loadPaymentPort.loadPaymentById(onlinePaymentInfo.getPaymentId())
            .orElseThrow(() -> new PaymentNotFoundException(onlinePaymentInfo.getPaymentId()));

   
    if (!"PENDING".equals(payment.getPaymentStatus().value())) return;

   
    Payment.UpdateInfo updateInfo = Payment.UpdateInfo.builder()
            .paymentId(payment.getPaymentId())
            .currency(payment.getCurrency())
            .paymentMethod(payment.getPaymentMethod())
            .paymentStatus(new PaymentStatus("SUCCESS"))
            .build();
    
    final var saved=savePaymentPort.save(payment.applyUpdateInfo(updateInfo));

    
   
        //onlinePaymentInfo.setTransactionId(new TransactionId(""));
    
    //saveOnlinePaymentInfoPort.save(onlinePaymentInfo);
    final var newPaymentSuccessEvent = PaymentSuccessDocument.builder().eventId(UUID.randomUUID()).orderId(saved.getOrderId().value())
    .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null).build();
        System.out.println("Publish payment success event");
    publishEventPort.publishPaymentSuccess(paymentSuccessRepo.save(newPaymentSuccessEvent));
    final var newOnlinePaymentSuccess = OnlinePaymentSuccessDocument.builder().eventId(UUID.randomUUID())
    .orderId(saved.getOrderId().value())
    .userEmail(saved.getUserEmail().value())
    .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null).build();
    publishEventPort.publishOnlinePaymentSuccess(onlinePaymentSuccessRepo.save(newOnlinePaymentSuccess));
}

}
