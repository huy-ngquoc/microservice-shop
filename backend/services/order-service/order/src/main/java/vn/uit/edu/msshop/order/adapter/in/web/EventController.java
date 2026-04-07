package vn.uit.edu.msshop.order.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedSuccessDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.inventory.OrderCreatedInventoryDocumentRepository;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final OrderCreatedInventoryDocumentRepository orderCreatedInventoryDocumentRepo;
    private final OrderCreatedDocumentRepository orderCreatedDocumentRepo;
    private final OrderCreatedSuccessDocumentRepository orderCreatedSuccessDocumentRepo;

    @DeleteMapping("/clear_events")
    public ResponseEntity<Void> deleteAllEvent() {
        orderCreatedInventoryDocumentRepo.deleteAll();
        orderCreatedDocumentRepo.deleteAll();
        orderCreatedSuccessDocumentRepo.deleteAll();
        
        return ResponseEntity.noContent().build();
        
    }

}
