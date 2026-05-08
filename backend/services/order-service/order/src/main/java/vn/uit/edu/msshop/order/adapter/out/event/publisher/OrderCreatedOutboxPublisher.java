package vn.uit.edu.msshop.order.adapter.out.event.publisher;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedDocumentRepository;
import vn.uit.edu.msshop.order.domain.event.OrderCreated;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedOutboxPublisher {

    private final OrderCreatedDocumentRepository orderCreatedDocumentRepo;
    private final CreatePaymentService createPaymentService;
    private final KafkaTemplate<String,OrderCreated> orderCreatedTemplate;
    
    @Scheduled(fixedDelay=10000)
    public void publishPendingEvents() {
        List<OrderCreatedDocument> pendingEvents =orderCreatedDocumentRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");
        
        for (OrderCreatedDocument event : pendingEvents) {
            if(event.getPaymentMethod().equals("ONLINE")) {
           createPaymentService.createPayment(event);
            }

           OrderCreated orderCreated = new OrderCreated(event.getEventId(),event.getCurrency(), event.getOrderId(), event.getPaymentMethod(), event.getPaymentValue(), event.getUserId(), event.getUserEmail());
        orderCreatedTemplate.send("order-topic",orderCreated);
        }
    }

    
}
