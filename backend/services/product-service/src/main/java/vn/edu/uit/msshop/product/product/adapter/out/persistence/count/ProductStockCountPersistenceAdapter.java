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
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductStockCountBulkDecrementPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductStockCountDeletionByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductStockCountBulkIncrementPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductStockCountInitializationByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductStockCountBulkLookupByProductIdsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductStockCountLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductStockCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Component
@RequiredArgsConstructor
@Slf4j
class ProductStockCountPersistenceAdapter
        implements
        ProductStockCountLookupByProductIdPort,
        ProductStockCountBulkLookupByProductIdsPort,
        ProductStockCountInitializationByProductIdPort,
        ProductStockCountBulkIncrementPort,
        ProductStockCountBulkDecrementPort,
        ProductStockCountDeletionByProductIdPort {

    private final ProductStockCountMongoRepository repository;
    private final ProductStockCountPersistenceMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public ProductStockCount loadByProductIdOrZero(
            final ProductId productId) {
        final var jpaProductId = productId.value();

        return this.repository.findById(jpaProductId)
                .map(this.mapper::toDomain)
                .orElseGet(() -> ProductStockCount.zero(productId));
    }

    @Override
    public Map<ProductId, ProductStockCount> loadAllByProductIds(
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
                        ProductStockCount::getProductId,
                        Function.identity()));
    }

    @Override
    public ProductStockCount initializeByProductId(
            final ProductId productId) {
        final var jpaProductId = productId.value();

        final var query = new Query(Criteria.where("_id").is(jpaProductId));
        final var update = new Update()
                .setOnInsert(ProductStockCountDocument.Fields.value, 0)
                .setOnInsert(ProductStockCountDocument.Fields.lastUpdatedTime, Instant.now());

        return this.upsertAndReturnDomain(query, update);
    }

    @Override
    public void increaseAll(
            final Map<ProductId, Integer> incrementByProductId) {
        if (incrementByProductId.isEmpty()) {
            return;
        }

        final var instantNow = Instant.now();
        final var bulkOps = this.mongoTemplate.bulkOps(
                BulkOperations.BulkMode.UNORDERED,
                ProductStockCountDocument.class);

        for (final var entry : incrementByProductId.entrySet()) {
            final var inc = entry.getValue();
            if (inc <= 0) {
                continue;
            }

            final var productId = entry.getKey();
            final var jpaProductId = productId.value();

            final var query = new Query(Criteria.where("_id").is(jpaProductId));
            final var update = new Update()
                    .inc(ProductStockCountDocument.Fields.value, inc)
                    .set(ProductStockCountDocument.Fields.lastUpdatedTime, instantNow);

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
                ProductStockCountDocument.class);

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
                            .and(ProductStockCountDocument.Fields.value).gte(dec));
            final var update = new Update()
                    .inc(ProductStockCountDocument.Fields.value, -dec)
                    .set(ProductStockCountDocument.Fields.lastUpdatedTime, instantNow);
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
                    "ProductStockCount decrease drift: expected={}, modified={}, productIds={}. "
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

    private ProductStockCount upsertAndReturnDomain(
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
                ProductStockCountDocument.class);
        Objects.requireNonNull(doc, "find-and-modify with upsert must return a non-null document");

        return this.mapper.toDomain(doc);
    }
}
