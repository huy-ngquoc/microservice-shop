package vn.uit.edu.msshop.notification.adapter.in.web;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.notification.adapter.out.event.EventDocument;
import vn.uit.edu.msshop.notification.adapter.out.event.EventDocumentRepository;
import vn.uit.edu.msshop.notification.application.dto.command.CreateEmailCommand;
import vn.uit.edu.msshop.notification.application.port.in.SendMailUseCase;
import vn.uit.edu.msshop.notification.domain.event.OnlinePaymentExpired;
import vn.uit.edu.msshop.notification.domain.event.OnlinePaymentSuccess;
import vn.uit.edu.msshop.notification.domain.event.PaymentLinkCreated;

@KafkaListener(topics="payment-online-topic", groupId="notification-group")
@RequiredArgsConstructor
public class NotificationPaymentEventListener {
    private final EventDocumentRepository eventDocumentRepo;
    private final SendMailUseCase sendMailUseCase;
    private final EmailMessageConverter messageConverter;
    @KafkaHandler
    public void onPaymentLinkCreated(PaymentLinkCreated event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        CreateEmailCommand command = messageConverter.toCommand(messageConverter.getPaymentLinkCreatedContent(event.getOrderId(), event.getPaymentLink()), "Tạo đơn hàng thành công", "ORDER_CREATED", event.getOrderId(), event.getUserEmail());
        sendMailUseCase.createEmail(command);
        eventDocumentRepo.save(new EventDocument(event.getEventId(),Instant.now()));
    }
    @KafkaHandler
    public void onPaymentLinkExpired(OnlinePaymentExpired event) {
        if(eventDocumentRepo.existsById(event.eventId())) return;
        CreateEmailCommand command = messageConverter.toCommand(messageConverter.getPaymentLinkExpiredContent(event.orderId()), "Đã hết hạn thanh toán", "PAYMENT_LINK_EXPIRED", event.orderId(), event.userEmail());
        sendMailUseCase.createEmail(command);
        eventDocumentRepo.save(new EventDocument(event.eventId(),Instant.now()));
    }
    @KafkaHandler
    public void onOnlinePaymentSuccess(OnlinePaymentSuccess event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        CreateEmailCommand command = messageConverter.toCommand(messageConverter.getPaymentLinkExpiredContent(event.getOrderId()), "Thanh toán online thành công", "ONLINE_PAYMET_SUCCESS", event.getOrderId(), event.getUserEmail());
        sendMailUseCase.createEmail(command);
        eventDocumentRepo.save(new EventDocument(event.getEventId(),Instant.now()));
    }

}
