package vn.uit.edu.msshop.order.adapter.out.event.publisher;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedDocumentRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedOutboxPublisher {

    private final OrderCreatedDocumentRepository orderCreatedDocumentRepo;
    private final CreatePaymentService createPaymentService;
    
    @Scheduled(fixedDelay=2000)
    public void publishPendingEvents() {
        List<OrderCreatedDocument> pendingEvents =orderCreatedDocumentRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");
        System.out.println("Call with size "+pendingEvents.size());
        for (OrderCreatedDocument event : pendingEvents) {
           createPaymentService.createPayment(event);
        }
    }

    
}
