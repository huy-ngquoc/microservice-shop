package vn.uit.edu.msshop.recommendation.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.recommendation.adapter.out.persistence.VariantMongoDbDocument;
import vn.uit.edu.msshop.recommendation.domain.model.Variant;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantId;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantImages;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantName;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantTargets;

@Component
public class VariantDocumentMapper {
    public Variant toDomain(VariantMongoDbDocument document) {
        final var snapshot = Variant.Snapshot.builder().id(new VariantId(document.getId()))
        .name(new VariantName(document.getName()))
        .targets(new VariantTargets(document.getTargets()))
        .images(new VariantImages(document.getImages()))
        .build();
        return Variant.reconstitue(snapshot);
    }
    public VariantMongoDbDocument toDocument(Variant domain) {
        return new VariantMongoDbDocument(domain.getId().value(), domain.getName().value(), domain.getTargets().value(), domain.getImages().value());
    }
}
