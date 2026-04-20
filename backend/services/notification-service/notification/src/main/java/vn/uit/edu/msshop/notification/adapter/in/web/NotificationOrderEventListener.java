package vn.uit.edu.msshop.notification.adapter.in.web;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.notification.adapter.out.event.EventDocument;
import vn.uit.edu.msshop.notification.adapter.out.event.EventDocumentRepository;
import vn.uit.edu.msshop.notification.application.dto.command.CreateEmailCommand;
import vn.uit.edu.msshop.notification.application.port.in.SendMailUseCase;
import vn.uit.edu.msshop.notification.domain.event.OrderCreated;

@KafkaListener(topics ="order-topic", groupId="notification-group")
@RequiredArgsConstructor
public class NotificationOrderEventListener {
    private final EventDocumentRepository eventDocumentRepo;
    private final SendMailUseCase sendMailUseCase;
    private final EmailMessageConverter messageConverter;
    @KafkaHandler
    public void onOrderCreated(OrderCreated event) {
        if(eventDocumentRepo.existsById(event.eventId())) return;
        if(event.paymentMethod().equals("COD")) {
            
            
            CreateEmailCommand command = messageConverter.toCommand(messageConverter.getCodOrderCreatedContent(event.orderId()), "Tạo đơn hàng thành công", "ORDER_CREATED", event.orderId(), event.userEmail());
            sendMailUseCase.createEmail(command);

        }
        eventDocumentRepo.save(new EventDocument(event.eventId(),Instant.now()));
    }
}
