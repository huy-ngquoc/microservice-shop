package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
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
import vn.edu.uit.msshop.product.product.adapter.out.persistence.mapper.ProductStockCountPersistenceMapper;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.DeleteProductStockCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.IncreaseAllProductStockCountsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.InitializeProductStockCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadAllProductStockCountsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductStockCountPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductStockCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Component
@RequiredArgsConstructor
public class ProductStockCountPersistenceAdapter
        implements
        LoadProductStockCountPort,
        LoadAllProductStockCountsPort,
        InitializeProductStockCountPort,
        IncreaseAllProductStockCountsPort,
        DeleteProductStockCountPort {
    private static final Collector<ProductStockCount, ?, Map<ProductId, ProductStockCount>> COLLECTOR = Collectors
            .toUnmodifiableMap(
                    ProductStockCount::getId,
                    Function.identity(),
                    (
                            existing,
                            replacement) -> existing);

    private final ProductStockCountMongoRepository repository;
    private final ProductStockCountPersistenceMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public ProductStockCount loadByIdOrZero(
            final ProductId id) {
        final var jpaId = id.value();

        return this.repository.findById(jpaId)
                .map(this.mapper::toDomain)
                .orElseGet(() -> ProductStockCount.zero(id));
    }

    @Override
    public Map<ProductId, ProductStockCount> loadAllByIds(
            final Collection<ProductId> ids) {
        if (ids.isEmpty()) {
            return Map.of();
        }

        final var jpaIds = ids.stream()
                .map(ProductId::value)
                .collect(Collectors.toSet());

        return this.repository.findAllById(jpaIds).stream()
                .map(this.mapper::toDomain)
                .collect(ProductStockCountPersistenceAdapter.COLLECTOR);
    }

    @Override
    public ProductStockCount initialize(
            final ProductId id) {
        final var jpaId = id.value();

        final var query = new Query(Criteria.where("_id").is(jpaId));
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
            final var id = entry.getKey();
            final var jpaId = id.value();
            final var increment = entry.getValue();

            final var query = new Query(Criteria.where("_id").is(jpaId));
            final var update = new Update()
                    .inc(ProductStockCountDocument.Fields.value, increment)
                    .set(ProductStockCountDocument.Fields.lastUpdatedTime, instantNow);

            bulkOps.upsert(query, update);
        }

        bulkOps.execute();
    }

    @Override
    public void deleteById(
            final ProductId id) {
        final var jpaId = id.value();
        this.repository.deleteById(jpaId);
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
