package vn.uit.edu.msshop.order.adapter.out.event.inventory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderCreated;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderDetail;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreatedInventoryOutboxPublisher {
    private final OrderCreatedInventoryDocumentRepository orderCreatedInventoryDocumentRepo;
    private final KafkaTemplate<String, OrderCreated> kafkaTemplate;
    private static final String PUBLISH_TOPIC="order-inventory";
    @Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<OrderCreatedInventoryDocument> pendingEvents =orderCreatedInventoryDocumentRepo.findTop50ByStatusOrderByCreatedAtAsc("PENDING");

        for (OrderCreatedInventoryDocument event : pendingEvents) {
            try {
                OrderCreated orderCreated = new OrderCreated(event.getEventId(),event.getOrderId(), event.getOrderDetails().stream().map(item->new OrderDetail(
                    item.getVariantId(), item.getAmount()
                )).toList());
                kafkaTemplate.send(PUBLISH_TOPIC, orderCreated)
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

    private void updateStatus(OrderCreatedInventoryDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        orderCreatedInventoryDocumentRepo.save(event);
    }
    private void handleFailure(OrderCreatedInventoryDocument event, String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    @Transactional
    public void markAsSent(OrderCreatedInventoryDocument event) {
        updateStatus(event, "SENT", null);
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    orderCreatedInventoryDocumentRepo.deleteByStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
