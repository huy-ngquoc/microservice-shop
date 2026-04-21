package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import vn.edu.uit.msshop.product.product.adapter.out.persistence.mapper.ProductSoldCountPersistenceMapper;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadAllProductSoldCountsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductSoldCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.DeleteProductSoldCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.IncreaseAllProductSoldCountsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.InitializeProductSoldCountPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Component
@RequiredArgsConstructor
public class ProductSoldCountPersistenceAdapter
        implements
        LoadProductSoldCountPort,
        LoadAllProductSoldCountsPort,
        InitializeProductSoldCountPort,
        IncreaseAllProductSoldCountsPort,
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
    public Optional<ProductSoldCount> loadById(
            final ProductId id) {
        final var jpaId = id.value();
        return this.repository.findById(jpaId)
                .map(this.mapper::toDomain);
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
                .setOnInsert(ProductSoldCountDocument.Fields.version, 0L)
                .setOnInsert(ProductSoldCountDocument.Fields.lastUpdatedTime, Instant.now());
        final var options = FindAndModifyOptions
                .options()
                .returnNew(true)
                .upsert(true);

        final var doc = this.mongoTemplate.findAndModify(
                query,
                update,
                options,
                ProductSoldCountDocument.class);
        return this.mapper.toDomain(doc);
    }

    @Override
    public void increaseAll(
            Map<ProductId, Integer> incrementByProductId) {
        if (incrementByProductId.isEmpty()) {
            return;
        }

        final var ops = this.mongoTemplate.bulkOps(
                BulkOperations.BulkMode.UNORDERED,
                ProductSoldCountDocument.class);
        for (final var entry : incrementByProductId.entrySet()) {
            final var query = new Query(Criteria.where("_id").is(entry.getKey().value()));
            final var update = new Update()
                    .inc(ProductSoldCountDocument.Fields.soldCount, entry.getValue())
                    .set(ProductSoldCountDocument.Fields.lastUpdatedTime, Instant.now())
                    .setOnInsert(ProductSoldCountDocument.Fields.version, 0L);
            ops.upsert(query, update);
        }
        ops.execute();
    }

    @Override
    public void deleteById(
            final ProductId id) {
        final var jpaId = id.value();
        this.repository.deleteById(jpaId);
    }
}
