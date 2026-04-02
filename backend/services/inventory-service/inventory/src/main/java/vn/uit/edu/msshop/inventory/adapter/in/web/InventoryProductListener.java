package vn.uit.edu.msshop.inventory.adapter.in.web;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.adapter.out.event.documents.EventDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.inventory.application.port.in.CreateInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.in.DeleteInventoryUseCase;
import vn.uit.edu.msshop.inventory.domain.event.VariantCreated;
import vn.uit.edu.msshop.inventory.domain.event.VariantDeleted;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@RestController
@RequiredArgsConstructor
@KafkaListener(topics="product-inventory",groupId="product-group")
public class InventoryProductListener {
    private final CreateInventoryUseCase createUseCase;
    private final DeleteInventoryUseCase deleteUseCase;
    private final EventDocumentRepository eventDocumentRepo;
    
    @KafkaHandler
    public void onVariantCreated(VariantCreated event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        createUseCase.create(new VariantId(event.getVariantId()));
        eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
    }

    @KafkaHandler
    public void onVariantDeleted(VariantDeleted event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        deleteUseCase.deleteByVariantId(new VariantId(event.getVariantId()));
        eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
    }

}
