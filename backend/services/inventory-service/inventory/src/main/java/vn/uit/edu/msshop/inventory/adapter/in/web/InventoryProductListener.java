package vn.uit.edu.msshop.inventory.adapter.in.web;

import java.util.List;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.application.port.in.CreateInventoryUseCase;
import vn.uit.edu.msshop.inventory.domain.event.product.ProductCreated;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Component
@RequiredArgsConstructor
@KafkaListener(topics="product-topic", groupId="product-group")
public class InventoryProductListener {
    private final CreateInventoryUseCase createUseCase;
    @KafkaHandler
    public void onProductCreated(ProductCreated productCreated) {
        List<VariantId> variantIds = productCreated.getVariantCreateds().stream().map(item->new VariantId(item.getVariantId())).toList();
        createUseCase.createNewsFromListVariantId(variantIds);
    }
}
