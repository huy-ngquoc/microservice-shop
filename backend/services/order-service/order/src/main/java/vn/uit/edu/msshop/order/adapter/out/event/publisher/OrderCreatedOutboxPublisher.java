package vn.uit.edu.msshop.order.adapter.out.event.publisher;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Value("${custom.call_payment_batch}")
    private String paymentBatch;
    @Scheduled(fixedRateString="${custom.call_payment_interval}")
    public void publishPendingEvents() {
        Pageable pageable = PageRequest.of(0, Integer.parseInt(paymentBatch));
        List<OrderCreatedDocument> pendingEvents =orderCreatedDocumentRepo.findByEventStatusOrderByCreatedAtAsc("PENDING", pageable).getContent();
        
        for (OrderCreatedDocument event : pendingEvents) {
            if("ONLINE".equals(event.getPaymentMethod())) {
           createPaymentService.createPayment(event);
            }

           OrderCreated orderCreated = new OrderCreated(event.getEventId(),event.getCurrency(), event.getOrderId(), event.getPaymentMethod(), event.getPaymentValue(), event.getUserId(), event.getUserEmail());
        orderCreatedTemplate.send("order-topic",orderCreated);
        }
    }

    
}
