package vn.uit.edu.msshop.order.adapter.out.persistence;

import java.util.List;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class OrderOutboxWorker {
    private final OrderOutboxRepository orderOutboxRepo;
    private final OrderOutboxProcessor processor;
    private final TaskExecutor taskExecutor;
    @Scheduled(fixedRate=5000)
    public void proccessPendingOrder() {
        List<OrderOutbox> outboxes = orderOutboxRepo.findTop50ByOutboxStatusOrderByCreatedAtAsc("PENDING");
        //System.out.println(outboxes.size());
        for(OrderOutbox outbox: outboxes) {
            processor.updateStatus(outbox);
        }
    //System.out.println("Kết thúc một lượt quét.");

   
   
        
    }
    
    
}
