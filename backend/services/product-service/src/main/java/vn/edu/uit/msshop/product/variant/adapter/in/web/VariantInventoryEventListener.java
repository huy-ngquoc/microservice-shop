package vn.edu.uit.msshop.product.variant.adapter.in.web;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.shared.event.EventDocument;
import vn.edu.uit.msshop.product.shared.event.EventDocumentRepository;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.SaveVariantPort;
import vn.edu.uit.msshop.product.variant.domain.event.InventoryUpdated;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantStock;

@KafkaListener(topics="inventory-product")
@Component
@RequiredArgsConstructor
public class VariantInventoryEventListener {
    private final LoadVariantPort loadVariantPort;
    private final EventDocumentRepository eventDocumentRepo;
    private final SaveVariantPort saveVariantPort;
    
    @KafkaHandler
    @Transactional
    @Nullable
    public void onInventoryUpdate(InventoryUpdated event) {
        if(event.getEventId()==null||event.getVariantId()==null) {
            return;
        }
        if(eventDocumentRepo.existsById(event.getEventId())) return;
       loadVariantPort.loadById(new VariantId(event.getVariantId()))
        .ifPresent(variant -> {
            var toSave = variant.updateStock(new VariantStock(event.getNewQuantity()));
            saveVariantPort.save(toSave);
            eventDocumentRepo.save(new EventDocument(event.getEventId(), Instant.now()));
        });
        
        
    }
}
