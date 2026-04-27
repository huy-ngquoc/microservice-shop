package vn.uit.edu.msshop.order.adapter.out.persistence;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.remote.InventoryChecker;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;

@Component
@RequiredArgsConstructor
public class OrderOutboxWorker {
    private final OrderOutboxRepository orderOutboxRepo;
    private final LoadOrderPort loadPort;
    private final SaveOrderPort savePort;
    private final InventoryChecker inventoryChecker;
    @Scheduled(fixedRate=5000)
    public void proccessPendingOrder() {
        List<OrderOutbox> outboxes = orderOutboxRepo.findTop50ByOutboxStatusOrderByCreatedAtAsc("PENDING");
        for(OrderOutbox outbox: outboxes) {
            updateStatus(outbox);
        }
    }
    @Transactional
    public void updateStatus(OrderOutbox outbox) {
        Order order= loadPort.loadById(new OrderId(outbox.getOrderId())).orElse(null);
            
            if(order==null) {
                outbox.setOutboxStatus("COMPLETED");
                orderOutboxRepo.save(outbox);
                return;
            }
        try {
            ResponseEntity<String> processResult = inventoryChecker.process(outbox);
            String result = processResult.getBody();
            if(result.equals("Trung du lieu")) {
                final var toSave=order.updateStatus(new OrderStatus("CONFIRMED"));
                
                //ToDo: them logic ban su kien gui mail, tao payment
                outbox.setOutboxStatus("COMPLETED");
                savePort.save(toSave);
                orderOutboxRepo.save(outbox);
            }
        }
        catch(RuntimeException e) {
            return;
        }
            
    }
    
}
