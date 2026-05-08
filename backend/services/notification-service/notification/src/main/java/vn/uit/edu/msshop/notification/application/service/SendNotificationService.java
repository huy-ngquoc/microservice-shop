package vn.uit.edu.msshop.notification.application.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.notification.application.port.in.SendNotificationUseCase;
import vn.uit.edu.msshop.notification.domain.model.Email;
@Service
@RequiredArgsConstructor
public class SendNotificationService implements SendNotificationUseCase {
    private final SimpMessagingTemplate messagingTemplate;
    @Override
    public void sendNotification( Email email) {
        String destination = "/topic/order/" + email.getUserId().value().toString();
        messagingTemplate.convertAndSend(destination, email);
    }
    
}
