package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
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
import vn.edu.uit.msshop.product.product.adapter.out.persistence.mapper.ProductSoldCountPersistenceMapper;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadAllProductSoldCountsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductSoldCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.DecreaseAllProductSoldCountsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.DeleteProductSoldCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.IncreaseAllProductSoldCountsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.InitializeProductSoldCountPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductSoldCountPersistenceAdapter
        implements
        LoadProductSoldCountPort,
        LoadAllProductSoldCountsPort,
        InitializeProductSoldCountPort,
        IncreaseAllProductSoldCountsPort,
        DecreaseAllProductSoldCountsPort,
        DeleteProductSoldCountPort {
    private static final Collector<ProductSoldCount, ?, Map<ProductId, ProductSoldCount>> COLLECTOR = Collectors
            .toUnmodifiableMap(
                    ProductSoldCount::getId,
                    Function.identity(),
                    (
                            existing,
                            replacement) -> existing);

    private final ProductSoldCountMongoRepository repository;
    private final ProductSoldCountPersistenceMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public ProductSoldCount loadByIdOrZero(
            final ProductId id) {
        final var jpaId = id.value();
        return this.repository.findById(jpaId)
                .map(this.mapper::toDomain)
                .orElseGet(() -> ProductSoldCount.zero(id));
    }

    @Override
    public Map<ProductId, ProductSoldCount> loadAllByIds(
            final Set<ProductId> ids) {
        if (ids.isEmpty()) {
            return Map.of();
        }

        final var jpaIds = ids.stream()
                .map(ProductId::value)
                .toList();
        return this.repository.findAllById(jpaIds).stream()
                .map(this.mapper::toDomain)
                .collect(ProductSoldCountPersistenceAdapter.COLLECTOR);
    }

    @Override
    public ProductSoldCount initialize(
            final ProductId id) {
        final var jpaId = id.value();

        final var query = new Query(Criteria.where("_id").is(jpaId));
        final var update = new Update()
                .setOnInsert(ProductSoldCountDocument.Fields.soldCount, 0)
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
            final var query = new Query(Criteria.where("_id").is(entry.getKey().value()));
            final var update = new Update()
                    .inc(ProductSoldCountDocument.Fields.soldCount, entry.getValue())
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

            final var id = entry.getKey();
            final var jpaId = id.value();

            final var query = new Query(Criteria.where("_id").is(jpaId)
                    .and(ProductSoldCountDocument.Fields.soldCount).gte(dec));
            final var update = new Update()
                    .inc(ProductSoldCountDocument.Fields.soldCount, -dec)
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
    public void deleteById(
            final ProductId id) {
        final var jpaId = id.value();
        this.repository.deleteById(jpaId);
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
