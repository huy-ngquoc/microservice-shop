package vn.uit.edu.msshop.order.adapter.in.web;

import java.time.Instant;
import java.util.List;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.out.event.documents.EventDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.persistence.VariantInfo;
import vn.uit.edu.msshop.order.adapter.out.persistence.VariantInfoRepository;
import vn.uit.edu.msshop.order.domain.event.product.ProductCreated;
import vn.uit.edu.msshop.order.domain.event.product.ProductDeleted;
import vn.uit.edu.msshop.order.domain.event.product.ProductUpdate;
import vn.uit.edu.msshop.order.domain.event.product.VariantDeleted;
import vn.uit.edu.msshop.order.domain.event.product.VariantUpdate;

@Component
@RequiredArgsConstructor
@KafkaListener(topics="product-topic",groupId="product-group")
public class OrderProductEventListener {
    private final VariantInfoRepository variantInfoRepo;
    private final EventDocumentRepository eventDocumentRepo;
    @KafkaHandler
    public void onProductUpdate(ProductUpdate event)
    {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        List<VariantInfo> variantInfos = variantInfoRepo.findByProductId(event.getProductId());
        for(VariantInfo v: variantInfos) {
            v.setProductName(event.getName());
        }
        variantInfoRepo.saveAll(variantInfos);
        eventDocumentRepo.save(new EventDocument(event.getEventId(), Instant.now()));
    }

    @KafkaHandler
    public void onVariantUpdate(VariantUpdate event) {
        System.out.println("Received variant update");
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        VariantInfo v = variantInfoRepo.findById(event.getVariantId()).orElse(null);
        if(v==null) v= new VariantInfo(event.getVariantId(), event.getProductId(),event.getProductName(), event.getPrice(), event.getTraits(), event.getImageKey());
        else {
            v.setPrice(event.getPrice());
            v.setTraits(event.getTraits());
            v.setImageKey(event.getImageKey());
        }
        variantInfoRepo.save(v);
        eventDocumentRepo.save(new EventDocument(event.getEventId(), Instant.now()));

    }
    @KafkaHandler
    public void onProductDelete(ProductDeleted event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        List<VariantInfo> variantInfos = variantInfoRepo.findByProductId(event.getProductId());
         variantInfoRepo.deleteAll(variantInfos);
        eventDocumentRepo.save(new EventDocument(event.getEventId(), Instant.now()));
    }
    @KafkaHandler
    public void onVariantDelete(VariantDeleted event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        variantInfoRepo.deleteById(event.getVariantId());
        eventDocumentRepo.save(new EventDocument(event.getEventId(), Instant.now()));
    }
    @KafkaHandler
    public void onProductCreated(ProductCreated event) {
        System.out.println("Product created event received");
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        List<VariantInfo> infos = event.getVariantCreateds().stream().map(item->new VariantInfo(item.getVariantId(), event.getProductId(),event.getProductName(), item.getPrice(), item.getTraits(), item.getImageKey())).toList();
        variantInfoRepo.saveAll(infos);
        eventDocumentRepo.save(new EventDocument(event.getEventId(), Instant.now()));
    }
    @KafkaHandler(isDefault = true)
    public void ignoreOthers(Object event) {
        
    }
}
