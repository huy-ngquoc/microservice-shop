package vn.uit.edu.msshop.inventory.adapter.in.web;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.adapter.out.event.documents.EventDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.inventory.application.port.in.CreateInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.in.DeleteInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.out.DeleteRedisPort;
import vn.uit.edu.msshop.inventory.application.port.out.LoadFromRedisPort;
import vn.uit.edu.msshop.inventory.application.port.out.LoadInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.SaveInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.SyncInventoryPort;
import vn.uit.edu.msshop.inventory.config.RedisConfig;
import vn.uit.edu.msshop.inventory.domain.event.product.VariantDeleted;
import vn.uit.edu.msshop.inventory.domain.event.product.VariantPurge;
import vn.uit.edu.msshop.inventory.domain.event.product.VariantRestore;
import vn.uit.edu.msshop.inventory.domain.event.product.VariantUpdate;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Component
@RequiredArgsConstructor
@KafkaListener(topics="product-topic", groupId="product-inventory-group")
public class InventoryProductListener {
    private final CreateInventoryUseCase createUseCase;
    private final EventDocumentRepository eventDocumentRepo;
    private final LoadInventoryPort loadPort;
    private final DeleteInventoryUseCase deleteUseCase;
    private final SaveInventoryPort savePort;
    private final LoadFromRedisPort loadFromRedisPort;
    private final SyncInventoryPort syncPort;
    private final RedisTemplate<String,Map<String,String>> redisTemplate;
    private final RedisConfig redisConfig;
    private final DeleteRedisPort deleteRedisPort;
    /*@KafkaHandler
    @Transactional
    public void onProductCreated(ProductCreated productCreated) {
        System.out.println("Received event from product service");
        if(eventDocumentRepo.existsById(productCreated.getEventId())) return;
        List<VariantId> variantIds = productCreated.getVariantCreateds().stream().map(item->new VariantId(item.getVariantId())).toList();
        createUseCase.createNewsFromListVariantId(variantIds);
        eventDocumentRepo.save(EventDocument.builder().eventId(productCreated.getEventId()).receiveAt(Instant.now()).build());
    }*/
    @KafkaHandler
    @Transactional
    public void onVariantUpdate(VariantUpdate event) {
        System.out.println("Receive variant updatedddd");
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        Inventory i = loadFromRedisPort.loadById(new VariantId(event.getVariantId()));
        if(i==null) {
            createUseCase.create(new VariantId(event.getVariantId()));
        }
        else {
            updateInventoryStatus(i.getVariantId().value(), "ENABLE", redisConfig.getUpdateInventoryStatus());
        }
        eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
    }
    @KafkaHandler
    @Transactional
    public void onVariantPurge(VariantPurge event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        deleteRedisPort.delete(new VariantId(event.getVariantId()));
        deleteUseCase.deleteByVariantId(new VariantId(event.getVariantId()));
        eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
    }
    @KafkaHandler
    @Transactional
    public void onVariantRestore(VariantRestore event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        Inventory i = loadFromRedisPort.loadById(new VariantId(event.getVariantId()));
        if(i==null) {

        }
        else {
            updateInventoryStatus(i.getVariantId().value(), "ENABLE", redisConfig.getUpdateInventoryStatus());
            

        }
        eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
    }

    @KafkaHandler
    //@Transactional
    public void onVariantDeleted(VariantDeleted event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        System.out.println("On variant deleted");
        Inventory i = loadFromRedisPort.loadById(new VariantId(event.getVariantId()));
        if(i==null) {
            System.out.println("I is null");
        }
        else {
            updateInventoryStatus(i.getVariantId().value(), "DISABLE", redisConfig.getUpdateInventoryStatus());
            //savePort.save(toSave);

        }
        eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
    }

    @KafkaHandler(isDefault = true)
    public void ignoreOthers(Object event) {
        System.out.println("Received strange event, ignore");
    }
    private void updateInventoryStatus(UUID variantId, String status, DefaultRedisScript<Long> updateStatusScript) {
    String key = "inventory:variant:" + variantId.toString();
    
    
    redisTemplate.execute(
        updateStatusScript, 
        List.of(key), 
        status
    );
}



}
