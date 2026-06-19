package vn.edu.uit.msshop.product.variant.adapter.out.persistence.variant;

import org.bson.Document;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.PartialIndexFilter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VariantIndexInitializer
        implements ApplicationRunner {

    private final MongoTemplate mongoTemplate;

    @Override
    public void run(
            final ApplicationArguments args) {
        final var indexOps = this.mongoTemplate.indexOps(VariantDocument.class);

        final var uniqueKeys = new Document()
                .append(VariantDocument.Fields.productId, 1)
                .append(VariantDocument.Fields.traitsKey, 1);
        final var activeOnlyFilter = PartialIndexFilter.of(
                Criteria.where(VariantDocument.Fields.deletionTime).is(null));
        final var uniqueProductTraitsIndex = new CompoundIndexDefinition(uniqueKeys)
                .named(VariantIndexNames.UQ_PRODUCT_TRAITS_ACTIVE)
                .unique()
                .partial(activeOnlyFilter);

        final var productIndex = new Index()
                .on(VariantDocument.Fields.productId, Sort.Direction.ASC)
                .named(VariantIndexNames.IDX_PRODUCT);

        indexOps.createIndex(uniqueProductTraitsIndex);
        indexOps.createIndex(productIndex);
    }
}
