package vn.edu.uit.msshop.product.product.adapter.out.sync;

import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.VariantSoldCountResponse;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.SaveProductPort;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.Amount;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.SaveVariantPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCount;

@Component
@RequiredArgsConstructor
public class SyncSoldCountJob {
    private final SoldCountFetcher soldCountFetcher;
    private final LoadVariantPort loadVariantPort;
    private final LoadProductPort loadProductPort;
    private final SaveProductPort saveProductPort;
    private final SaveVariantPort saveVariantPort;
    @Scheduled(fixedRate=180000) 
    @Transactional
    public void updateSoldCount() {
        List<VariantSoldCountResponse> response = soldCountFetcher.getSoldCounts();
        List<VariantId> variantIds =  response.stream().map(item->new VariantId(item.getVariantId())).toList();
        List<Variant> variants = loadVariantPort.loadByListIds(variantIds);
        List<Product> products = loadProductPort.loadByVariants(variants);
        List<Product> toSaves = new ArrayList<>();
        List<Variant> toSaveVariants =new ArrayList<>();
        for(VariantSoldCountResponse detail:response) {
            Variant v = findVariantInList(new VariantId(detail.getVariantId()), variants);
            if(v!=null) {
                Product p = findProductInListByVariant(v, products);
                if(p!=null) {
                    toSaves.add(p.updateSoldCount(new Amount(detail.getSoldCount())));
                    toSaveVariants.add(v.updateSoldCount(new VariantSoldCount(detail.getSoldCount())));
                }
            }
            
        }
        saveVariantPort.saveAll(toSaveVariants);
        saveProductPort.saveAll(toSaves);
    }
    private Variant findVariantInList(VariantId id, List<Variant> variants) {
        for(Variant v: variants) {
            if(v.getId().value().equals(id.value())) 
            {
                return v;
            }
        }
        return null;
    }
    private Product findProductInListByVariant(Variant v, List<Product> products) {
        for(Product p: products) {
            if(v.getProductId().value().equals(p.getId().value())) {
                return p;
            }
        }
        return null;
    }
}
