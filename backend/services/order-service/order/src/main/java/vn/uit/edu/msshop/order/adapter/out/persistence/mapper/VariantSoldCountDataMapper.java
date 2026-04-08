package vn.uit.edu.msshop.order.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.order.adapter.out.persistence.VariantSoldCountDocument;
import vn.uit.edu.msshop.order.domain.model.VariantSoldCount;
import vn.uit.edu.msshop.order.domain.model.valueobject.SoldCount;
import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;
import vn.uit.edu.msshop.order.domain.model.valueobject.Version;

@Component
public class VariantSoldCountDataMapper {
    public VariantSoldCount toDomain(VariantSoldCountDocument document) {
        final var snapshot = VariantSoldCount.Snapshot.builder().id(new VariantId(document.getId())).soldCount(new SoldCount(document.getSoldCount())).version(new Version(document.getVersion())).build();
        return VariantSoldCount.reconstitue(snapshot); 
    }
    public VariantSoldCountDocument toDocument(VariantSoldCount domain) {
        return new VariantSoldCountDocument(domain.getId().value(), domain.getSoldCount().value(), domain.getVersion()==null?null:domain.getVersion().value());
    }
}
