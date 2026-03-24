package vn.uit.edu.msshop.inventory.adapter.in.web;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
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
    
    @KafkaHandler
    public void onVariantCreated(VariantCreated event) {
        createUseCase.create(new VariantId(event.getVariantId()));
    }

    @KafkaHandler
    public void onVariantDeleted(VariantDeleted event) {
        deleteUseCase.deleteByVariantId(new VariantId(event.getVariantId()));
    }

}
