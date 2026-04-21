package vn.uit.edu.payment.adapter.in.web;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.payment.adapter.in.web.mapper.PaymentWebMapper;
import vn.uit.edu.payment.adapter.out.event.documents.EventDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.payment.application.dto.command.CreatePaymentCommand;
import vn.uit.edu.payment.application.port.in.CreatePaymentUseCase;
import vn.uit.edu.payment.domain.event.OrderCreated;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics="order-topic",groupId="order-payment-group")
public class OrderEventListener {
    private final PaymentWebMapper mapper;
    private final CreatePaymentUseCase createUseCase;
    private final EventDocumentRepository eventDocumentRepo;

    @KafkaHandler
    public void handleOrderCreated(OrderCreated event) {
        System.out.println("Listen to event");
        if(!eventDocumentRepo.existsById(event.eventId())) {
        CreatePaymentCommand command = mapper.toCommand(event);
        createUseCase.create(command);
        eventDocumentRepo.save(new EventDocument(event.eventId(), Instant.now()));
        }
    }
    @KafkaHandler(isDefault=true) 
    public void onObjectReceived(Object event) {
        
    }
    

}
