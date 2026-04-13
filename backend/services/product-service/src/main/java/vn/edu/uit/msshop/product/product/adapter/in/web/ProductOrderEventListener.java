package vn.edu.uit.msshop.product.product.adapter.in.web;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.SaveProductPort;
import vn.edu.uit.msshop.product.product.domain.event.IncreaseSoldCountDetail;
import vn.edu.uit.msshop.product.product.domain.event.IncreaseSoldCountEvents;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.IncreaseAmount;
import vn.edu.uit.msshop.product.shared.event.EventDocument;
import vn.edu.uit.msshop.product.shared.event.EventDocumentRepository;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.SaveVariantPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Component
@KafkaListener(topics="order-product")
@RequiredArgsConstructor
public class ProductOrderEventListener {
    private final LoadProductPort loadProductPort;
    private final LoadVariantPort loadVariantPort;
    private final SaveProductPort saveProductPort;
    private final EventDocumentRepository eventDocumentRepo;
    private final SaveVariantPort saveVariantPort;
    @KafkaHandler
    @Transactional
    
    public void onOrderReceived(IncreaseSoldCountEvents event) {
        System.out.println("Event received");
        if(eventDocumentRepo.existsById(event.eventId())) return;
        List<VariantId> variantIds =  event.details().stream().map(item->new VariantId(item.variantId())).toList();
        System.out.println("Variant Ids size" +variantIds.size());
        List<Variant> variants = loadVariantPort.loadByListIds(variantIds);
        System.out.println("Variants size "+variants.size());
        List<Product> products = loadProductPort.loadByVariants(variants);
        System.out.println("Products size "+products.size());
        List<Product> toSaves = new ArrayList<>();
        List<Variant> toSaveVariants=  new ArrayList<>();
        for(IncreaseSoldCountDetail detail:event.details()) {
            Variant v = findVariantInList(new VariantId(detail.variantId()), variants);
            if(v!=null) {
                Product p = findProductInListByVariant(v, products);
                if(p!=null) {
                    toSaves.add(p.increaseSoldCount(new IncreaseAmount(detail.amount())));
                    toSaveVariants.add(v.increaseSoldCount(new vn.edu.uit.msshop.product.variant.domain.model.valueobject.IncreaseAmount(detail.amount())));
                }
            }
            
        }
        eventDocumentRepo.save(new EventDocument(event.eventId(), Instant.now()));
        saveProductPort.saveAll(toSaves);
        saveVariantPort.saveAll(toSaveVariants);

    }
    @Nullable
    private Variant findVariantInList(VariantId id, List<Variant> variants) {
        for(Variant v: variants) {
            if(v.getId().value().equals(id.value())) 
            {
                return v;
            }
        }
        return null;
    }
    @Nullable
    private Product findProductInListByVariant(Variant v, List<Product> products) {
        for(Product p: products) {
            if(v.getProductId().value().equals(p.getId().value())) {
                return p;
            }
        }
        return null;
    }
}
