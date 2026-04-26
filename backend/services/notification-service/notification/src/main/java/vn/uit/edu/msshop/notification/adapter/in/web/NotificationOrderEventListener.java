package vn.uit.edu.msshop.notification.adapter.in.web;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.notification.adapter.out.event.EventDocument;
import vn.uit.edu.msshop.notification.adapter.out.event.EventDocumentRepository;
import vn.uit.edu.msshop.notification.application.dto.command.CreateEmailCommand;
import vn.uit.edu.msshop.notification.application.port.in.SendMailUseCase;
import vn.uit.edu.msshop.notification.application.port.in.SendNotificationUseCase;
import vn.uit.edu.msshop.notification.domain.event.OrderCreated;
import vn.uit.edu.msshop.notification.domain.event.OrderUpdatedEvent;
@Component
@KafkaListener(topics ="order-topic", groupId="notification-group")
@RequiredArgsConstructor
public class NotificationOrderEventListener {
    private final EventDocumentRepository eventDocumentRepo;
    private final SendMailUseCase sendMailUseCase;
    private final EmailMessageConverter messageConverter;
    private final SendNotificationUseCase sendNotificationUseCase;
    @KafkaHandler
    public void onOrderCreated(OrderCreated event) {
        System.out.println("Order created");
        if(eventDocumentRepo.existsById(event.eventId())) return;
        if(event.paymentMethod().equals("COD")) {
            
            
            CreateEmailCommand command = messageConverter.toCommand(messageConverter.getCodOrderCreatedContent(event.orderId()), "Tạo đơn hàng thành công", "ORDER_CREATED", event.orderId(), event.userEmail(), event.userId());
            final var email=sendMailUseCase.createEmail(command);
            sendNotificationUseCase.sendNotification(email);

        }
        eventDocumentRepo.save(new EventDocument(event.eventId(),Instant.now()));
        
    }
    @KafkaHandler
    public void onOrderUpdated(OrderUpdatedEvent event) {
        
        
            System.out.println("On Order updated");
            System.out.println("Event Id"+event.getEventId().toString());
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        eventDocumentRepo.save(new EventDocument(event.getEventId(),Instant.now()));
        String content = "";
        String title="";
        String emailType="";
        if(event.getStatus().equals("RECEIVED")){
             content = messageConverter.getOrderReceivedContent(event.getOrderId());
             title="Đơn hàng đã nhận thành công";
             emailType="ORDER_RECEIVED";
        }
        else if(event.getStatus().equals("SHIPPING")){
             content = messageConverter.getOrderShippedContent(event.getOrderId());
             title="Đơn hàng đã được vận chuyển";
             emailType="ORDER_SHIPPED";
        }
        else if(event.getStatus().equals("CANCELLED")) {
            content = messageConverter.getOrderCancelledContent(event.getOrderId());
            title="Đơn hàng đã bị hủy";
             emailType="ORDER_CANCELLED";
        }
        else {
            return;
        }
        CreateEmailCommand command = messageConverter.toCommand(content, title, emailType,event.getOrderId() , event.getEmail(), event.getUserId());
        final var email=sendMailUseCase.createEmail(command);
        sendNotificationUseCase.sendNotification(email);

    }
    
        
    
    @KafkaHandler(isDefault=true)
    public void onObjectReceived(Object event) {
        System.out.println("Receive strange event");
    }
}
