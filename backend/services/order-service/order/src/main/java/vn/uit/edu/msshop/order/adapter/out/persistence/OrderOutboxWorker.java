package vn.uit.edu.msshop.order.adapter.out.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class OrderOutboxWorker {
    private final OrderOutboxRepository orderOutboxRepo;
    private final OrderOutboxProcessor processor;
    private final TaskExecutor taskExecutor;
    @Value("${custom.call_inventory_batch}")
    private String inventoryBatch;
   @Scheduled(fixedDelayString="${custom.call_inventory_interval}")
    public void proccessPendingOrder() {
        Pageable pageable = PageRequest.of(0,Integer.parseInt(inventoryBatch));
        List<OrderOutbox> outboxes = orderOutboxRepo.findByOutboxStatusOrderByCreatedAtAsc("PENDING", pageable).getContent();
        //System.out.println(outboxes.size());
        for(OrderOutbox outbox: outboxes) {
            processor.updateStatus(outbox);
        }
    //System.out.println("Kết thúc một lượt quét.");

   
   
        
    }
    
    
}
