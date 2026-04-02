package vn.uit.edu.msshop.order.adapter.out.event.publisher.inventory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.order.adapter.out.event.documents.inventory.OrderShippedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.inventory.OrderShippedDocumentRepository;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderDetail;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderShipped;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderShippedOutboxPublisher {
    private final OrderShippedDocumentRepository orderShippedDocumentRepo;
    private final KafkaTemplate<String, OrderShipped> kafkaTemplate;
    private static final String PUBLISH_TOPIC="order-inventory";
    @Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<OrderShippedDocument> pendingEvents =orderShippedDocumentRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (OrderShippedDocument event : pendingEvents) {
            try {
                OrderShipped orderShipped = new OrderShipped(event.getEventId(),event.getOrderId(), event.getOrderDetails().stream().map(item->new OrderDetail(item.getVariantId(), item.getAmount())).toList());
                kafkaTemplate.send(PUBLISH_TOPIC, orderShipped)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            
                            updateStatus(event, "SENT", null);
                        } else {
                            
                            handleFailure(event, ex.getMessage());
                        }
                    });
            } catch (Exception e) {
                handleFailure(event, e.getMessage());
            }
        }
    }

    private void updateStatus(OrderShippedDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        orderShippedDocumentRepo.save(event);
    }
    private void handleFailure(OrderShippedDocument event, String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    @Transactional
    public void markAsSent(OrderShippedDocument event) {
        updateStatus(event, "SENT", null);
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    orderShippedDocumentRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
