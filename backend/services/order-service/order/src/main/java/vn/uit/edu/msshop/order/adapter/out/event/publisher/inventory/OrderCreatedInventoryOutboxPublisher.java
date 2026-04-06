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
import vn.uit.edu.msshop.order.adapter.out.event.documents.inventory.OrderCreatedInventoryDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.inventory.OrderCreatedInventoryDocumentRepository;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderCreated;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderDetail;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreatedInventoryOutboxPublisher {
    private final OrderCreatedInventoryDocumentRepository orderCreatedInventoryDocumentRepo;
    private final KafkaTemplate<String, OrderCreated> kafkaTemplate;
    private static final String PUBLISH_TOPIC="order-inventory";
    //@Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        //System.out.println("Alo alo");
        List<OrderCreatedInventoryDocument> pendingEvents =orderCreatedInventoryDocumentRepo.findAll();
        //System.out.println("Size " +pendingEvents.size());
        for (OrderCreatedInventoryDocument event : pendingEvents) {
            
            if(event.getEventStatus()==null||!event.getEventStatus().equals("PENDING")) continue;
            try {
                //System.out.println(event.getEventStatus());
                OrderCreated orderCreated = new OrderCreated(event.getEventId(),event.getOrderId(), event.getOrderDetails().stream().map(item->new OrderDetail(
                    item.getVariantId(), item.getAmount()
                )).toList());
                kafkaTemplate.send(PUBLISH_TOPIC, orderCreated)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            System.out.println("Sent event with id "+orderCreated.getEventId());
                            updateStatus(event, "SENT", null);
                        } else {
                            //System.out.println(ex.getMessage());
                            handleFailure(event, ex.getMessage());
                        }
                    });
            } catch (Exception e) {
                
                
                handleFailure(event, e.getMessage());
            }
        }
    }
    @Transactional
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
    
    orderCreatedInventoryDocumentRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
