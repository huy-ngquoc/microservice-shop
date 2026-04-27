package vn.uit.edu.msshop.order.adapter.out.persistence;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderOutboxWorker {
    private final OrderOutboxRepository orderOutboxRepo;
    private final OrderOutboxProcessor processor;
    @Scheduled(fixedRate=5000)
    public void proccessPendingOrder() {
        List<OrderOutbox> outboxes = orderOutboxRepo.findTop50ByOutboxStatusOrderByCreatedAtAsc("PENDING");
        for(OrderOutbox outbox: outboxes) {
            processor.updateStatus(outbox);
        }
    }
    
    
}
