package vn.edu.uit.msshop.product.product.adapter.out.persistence.count;

import java.time.Instant;
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
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductSoldCountBulkDecrementPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductSoldCountDeletionByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductSoldCountBulkIncrementPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductSoldCountInitializationByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductSoldCountBulkLookupByProductIdsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductSoldCountLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Component
@RequiredArgsConstructor
@Slf4j
class ProductSoldCountPersistenceAdapter
        implements
        ProductSoldCountLookupByProductIdPort,
        ProductSoldCountBulkLookupByProductIdsPort,
        ProductSoldCountInitializationByProductIdPort,
        ProductSoldCountBulkIncrementPort,
        ProductSoldCountBulkDecrementPort,
        ProductSoldCountDeletionByProductIdPort {

    private final ProductSoldCountMongoRepository repository;
    private final ProductSoldCountPersistenceMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public ProductSoldCount loadByProductIdOrZero(
            final ProductId productId) {
        final var jpaProductId = productId.value();
        return this.repository.findById(jpaProductId)
                .map(this.mapper::toDomain)
                .orElseGet(() -> ProductSoldCount.zero(productId));
    }

    @Override
    public Map<ProductId, ProductSoldCount> loadAllByProductIds(
            final Set<ProductId> productIdSet) {
        if (productIdSet.isEmpty()) {
            return Map.of();
        }

        final var jpaProductIdSet = productIdSet.stream()
                .map(ProductId::value)
                .collect(Collectors.toUnmodifiableSet());
        final var docList = this.repository.findAllById(jpaProductIdSet);

        return docList.stream()
                .map(this.mapper::toDomain)
                .collect(Collectors.toUnmodifiableMap(
                        ProductSoldCount::getProductId,
                        Function.identity()));
    }

    @Override
    public ProductSoldCount initializeByProductId(
            final ProductId productId) {
        final var jpaProductId = productId.value();

        final var query = new Query(Criteria.where("_id").is(jpaProductId));
        final var update = new Update()
                .setOnInsert(ProductSoldCountDocument.Fields.value, 0)
                .setOnInsert(ProductSoldCountDocument.Fields.lastUpdatedTime, Instant.now());

        return this.upsertAndReturnDomain(query, update);
    }

    @Override
    public void increaseAll(
            Map<ProductId, Integer> incrementByProductId) {
        if (incrementByProductId.isEmpty()) {
            return;
        }

        final var instantNow = Instant.now();
        final var bulkOps = this.mongoTemplate.bulkOps(
                BulkOperations.BulkMode.UNORDERED,
                ProductSoldCountDocument.class);

        for (final var entry : incrementByProductId.entrySet()) {
            final var inc = entry.getValue();
            if (inc <= 0) {
                continue;
            }

            final var productId = entry.getKey();
            final var jpaProductId = productId.value();

            final var query = new Query(Criteria.where("_id").is(jpaProductId));
            final var update = new Update()
                    .inc(ProductSoldCountDocument.Fields.value, inc)
                    .set(ProductSoldCountDocument.Fields.lastUpdatedTime, instantNow);
            bulkOps.upsert(query, update);
        }

        bulkOps.execute();
    }

    @Override
    public void decreaseAll(
            Map<ProductId, Integer> decrementByProductId) {
        if (decrementByProductId.isEmpty()) {
            return;
        }

        final var instantNow = Instant.now();
        final var bulkOps = this.mongoTemplate.bulkOps(
                BulkOperations.BulkMode.UNORDERED,
                ProductSoldCountDocument.class);

        int expectedOps = 0;
        for (final var entry : decrementByProductId.entrySet()) {
            final var dec = entry.getValue();
            if (dec <= 0) {
                continue;
            }

            final var productId = entry.getKey();
            final var jpaProductId = productId.value();

            final var query = new Query(
                    Criteria.where("_id").is(jpaProductId)
                            .and(ProductSoldCountDocument.Fields.value).gte(dec));
            final var update = new Update()
                    .inc(ProductSoldCountDocument.Fields.value, -dec)
                    .set(ProductSoldCountDocument.Fields.lastUpdatedTime, instantNow);
            bulkOps.updateOne(query, update);

            ++expectedOps;
        }

        if (expectedOps <= 0) {
            return;
        }

        final var result = bulkOps.execute();
        final var modified = result.getModifiedCount();

        if (modified < expectedOps) {
            log.warn(
                    "ProductSoldCount decrease drift: expected={}, modified={}, productIds={}. "
                            + "Possible causes: (1) document not initialized, (2) current value < decrement "
                            + "(state divergence between variant and product aggregate). "
                            + "Reconcile job should self-heal.",
                    expectedOps,
                    modified,
                    decrementByProductId.keySet());
        }
    }

    @Override
    public void deleteByProductId(
            final ProductId productId) {
        final var jpaProductId = productId.value();
        this.repository.deleteById(jpaProductId);
    }

    private ProductSoldCount upsertAndReturnDomain(
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
                ProductSoldCountDocument.class);
        Objects.requireNonNull(doc, "find-and-modify with upsert must return a non-null document");

        return this.mapper.toDomain(doc);
    }
}
