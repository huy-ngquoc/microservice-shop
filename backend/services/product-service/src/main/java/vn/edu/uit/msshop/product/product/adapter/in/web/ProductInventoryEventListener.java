package vn.edu.uit.msshop.product.product.adapter.in.web;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.domain.event.InventoryUpdated;

@Component
@KafkaListener(topics="inventory-product")
public class ProductInventoryEventListener {
    @KafkaHandler
    public void updateAmount(InventoryUpdated event) {
        
    }
}
