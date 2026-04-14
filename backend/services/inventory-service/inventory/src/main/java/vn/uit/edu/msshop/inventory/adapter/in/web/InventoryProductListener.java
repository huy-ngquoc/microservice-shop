package vn.uit.edu.msshop.inventory.adapter.in.web;

import java.time.Instant;
import java.util.List;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.adapter.out.event.documents.EventDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.inventory.application.port.in.CreateInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.in.DeleteInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.out.LoadInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.SaveInventoryPort;
import vn.uit.edu.msshop.inventory.domain.event.VariantDeleted;
import vn.uit.edu.msshop.inventory.domain.event.product.ProductCreated;
import vn.uit.edu.msshop.inventory.domain.event.product.VariantPurge;
import vn.uit.edu.msshop.inventory.domain.event.product.VariantRestore;
import vn.uit.edu.msshop.inventory.domain.event.product.VariantUpdate;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Component
@RequiredArgsConstructor
@KafkaListener(topics="product-topic", groupId="product-group")
public class InventoryProductListener {
    private final CreateInventoryUseCase createUseCase;
    private final EventDocumentRepository eventDocumentRepo;
    private final LoadInventoryPort loadPort;
    private final DeleteInventoryUseCase deleteUseCase;
    private final SaveInventoryPort savePort;
    @KafkaHandler
    @Transactional
    public void onProductCreated(ProductCreated productCreated) {
        if(eventDocumentRepo.existsById(productCreated.getEventId())) return;
        List<VariantId> variantIds = productCreated.getVariantCreateds().stream().map(item->new VariantId(item.getVariantId())).toList();
        createUseCase.createNewsFromListVariantId(variantIds);
        eventDocumentRepo.save(EventDocument.builder().eventId(productCreated.getEventId()).receiveAt(Instant.now()).build());
    }
    @KafkaHandler
    @Transactional
    public void onVariantUpdate(VariantUpdate event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        if(loadPort.loadByVariantId(new VariantId(event.getVariantId())).isEmpty()) {
            createUseCase.create(new VariantId(event.getVariantId()));
        }
        eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
    }
    @KafkaHandler
    @Transactional
    public void onVariantPurge(VariantPurge event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        deleteUseCase.deleteByVariantId(new VariantId(event.getVariantId()));
        eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
    }
    @KafkaHandler
    @Transactional
    public void onVariantRestore(VariantRestore event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        Inventory i = loadPort.loadByVariantId(new VariantId(event.getVariantId())).orElse(null);
        if(i==null) {

        }
        else {
            final var toSave=i.restore();
            savePort.save(toSave);

        }
        eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
    }

    @KafkaHandler
    @Transactional
    public void onVariantDeleted(VariantDeleted event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        Inventory i = loadPort.loadByVariantId(new VariantId(event.getVariantId())).orElse(null);
        if(i==null) {

        }
        else {
            final var toSave=i.disable();
            savePort.save(toSave);

        }
        eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
    }



}
