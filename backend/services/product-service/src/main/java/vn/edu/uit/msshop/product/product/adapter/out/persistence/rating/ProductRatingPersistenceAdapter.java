package vn.edu.uit.msshop.product.product.adapter.out.persistence.rating;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.command.ProductRatingDeletionByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.command.ProductRatingInitializationByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.command.ProductRatingBulkUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.command.ProductRatingUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingBulkLookupByProductIdsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Component
@RequiredArgsConstructor
class ProductRatingPersistenceAdapter
        implements
        ProductRatingLookupByProductIdPort,
        ProductRatingBulkLookupByProductIdsPort,
        ProductRatingInitializationByProductIdPort,
        ProductRatingUpdatePort,
        ProductRatingBulkUpdatePort,
        ProductRatingDeletionByProductIdPort {

    private final ProductRatingMongoRepository repository;
    private final ProductRatingPersistenceMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public ProductRating loadByProductIdOrZero(
            final ProductId productId) {
        final var jpaProductId = productId.value();
        return this.repository.findById(jpaProductId)
                .map(this.mapper::toDomain)
                .orElseGet(() -> ProductRating.zero(productId));
    }

    @Override
    public Map<ProductId, ProductRating> loadAllByProductIds(
            final Set<ProductId> productIdSet) {
        if (productIdSet.isEmpty()) {
            return Map.of();
        }

        final var jpaProductIdSet = productIdSet.stream()
                .map(ProductId::value)
                .toList();
        return this.repository.findAllById(jpaProductIdSet).stream()
                .map(this.mapper::toDomain)
                .collect(Collectors.toUnmodifiableMap(
                        ProductRating::getProductId,
                        Function.identity()));
    }

    @Override
    public ProductRating initializeByProductId(
            final ProductId productId) {
        final var jpaProductId = productId.value();

        final var query = new Query(Criteria.where("_id").is(jpaProductId));
        final var update = new Update()
                .setOnInsert(ProductRatingDocument.Fields.total, 0)
                .setOnInsert(ProductRatingDocument.Fields.amount, 0)
                .setOnInsert(ProductRatingDocument.Fields.lastUpdatedTime, Instant.now());

        return this.upsertAndReturnDomain(query, update);
    }

    @Override
    public ProductRating update(
            final ProductRating rating) {
        final var query = new Query(Criteria.where("_id").is(rating.getProductId().value()));
        final var update = new Update()
                .set(ProductRatingDocument.Fields.total, rating.getTotal().value())
                .set(ProductRatingDocument.Fields.amount, rating.getAmount().value())
                .set(ProductRatingDocument.Fields.lastUpdatedTime, Instant.now());

        return this.upsertAndReturnDomain(query, update);
    }

    @Override
    public void updateAll(
            final Collection<ProductRating> ratings) {
        if (ratings.isEmpty()) {
            return;
        }

        final var ops = this.mongoTemplate.bulkOps(
                BulkOperations.BulkMode.UNORDERED,
                ProductRatingDocument.class);
        final var instantNow = Instant.now();

        for (final var rating : ratings) {
            final var query = new Query(Criteria.where("_id").is(rating.getProductId().value()));
            final var update = new Update()
                    .set(ProductRatingDocument.Fields.total, rating.getTotal().value())
                    .set(ProductRatingDocument.Fields.amount, rating.getAmount().value())
                    .set(ProductRatingDocument.Fields.lastUpdatedTime, instantNow);
            ops.upsert(query, update);
        }

        ops.execute();
    }

    @Override
    public void deleteByProductId(
            final ProductId productId) {
        final var jpaProductId = productId.value();
        this.repository.deleteById(jpaProductId);
    }

    private ProductRating upsertAndReturnDomain(
            final Query query,
            final Update update) {
        final var options = FindAndModifyOptions
                .options()
                .returnNew(true)
                .upsert(true);
        final var doc = this.mongoTemplate.findAndModify(
                query,
                update,
                options,
                ProductRatingDocument.class);
        Objects.requireNonNull(doc, "find-and-modify with upsert must return a non-null document");

        return this.mapper.toDomain(doc);
    }
}
