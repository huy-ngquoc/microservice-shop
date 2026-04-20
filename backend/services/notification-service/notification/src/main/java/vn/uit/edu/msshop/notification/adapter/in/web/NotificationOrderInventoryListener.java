package vn.uit.edu.msshop.notification.adapter.in.web;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.notification.adapter.out.event.EventDocument;
import vn.uit.edu.msshop.notification.adapter.out.event.EventDocumentRepository;
import vn.uit.edu.msshop.notification.application.dto.command.CreateEmailCommand;
import vn.uit.edu.msshop.notification.application.port.in.SendMailUseCase;
import vn.uit.edu.msshop.notification.domain.event.OrderCancelled;
import vn.uit.edu.msshop.notification.domain.event.OrderShipped;

@KafkaListener(topics = "order-inventory", groupId="notification-group")
@RequiredArgsConstructor
public class NotificationOrderInventoryListener {
    private final EventDocumentRepository eventDocumentRepo;
    private final SendMailUseCase sendMailUseCase;
    private final EmailMessageConverter messageConverter;

    @KafkaHandler
    public void onOrderCancelled(OrderCancelled event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        CreateEmailCommand command = messageConverter.toCommand(messageConverter.getOrderCancelledContent(event.getOrderId()), "Hủy đơn hàng thành công", "ORDER_CANCELLED", event.getOrderId(), event.getUserEmail());
        sendMailUseCase.createEmail(command);
        eventDocumentRepo.save(new EventDocument(event.getEventId(),Instant.now()));
    } 
    @KafkaHandler
    public void onOrderShipped(OrderShipped event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        CreateEmailCommand command = messageConverter.toCommand(messageConverter.getOrderShippedContent(event.getOrderId()), "Đơn hàng đang được vận chuyển", "ORDER_CANCELLED", event.getOrderId(), event.getUserEmail());
        sendMailUseCase.createEmail(command);
        eventDocumentRepo.save(new EventDocument(event.getEventId(),Instant.now()));
    }
}
