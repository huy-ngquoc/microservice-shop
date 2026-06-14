package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantStockCountBulkDeletionByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantStockCountBulkInitializationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantStockCountBulkUpdatePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantStockCountDeletionByVariantIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantStockCountInitializationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantStockCountUpdatePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantStockCountBulkLookupByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantStockCountLookupByVariantIdPort;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Component
@RequiredArgsConstructor
public class VariantStockCountPersistenceAdapter
        implements
        VariantStockCountLookupByVariantIdPort,
        VariantStockCountBulkLookupByVariantIdsPort,
        VariantStockCountInitializationPort,
        VariantStockCountBulkInitializationPort,
        VariantStockCountUpdatePort,
        VariantStockCountBulkUpdatePort,
        VariantStockCountDeletionByVariantIdPort,
        VariantStockCountBulkDeletionByVariantIdsPort {
    private static final Collector<
            VariantStockCount,
            ?,
            Map<VariantId, VariantStockCount>> COLLECTOR = Collectors.toUnmodifiableMap(
                    VariantStockCount::getVariantId,
                    Function.identity(),
                    (
                            existing,
                            replacement) -> existing);

    private final VariantStockCountMongoRepository repository;
    private final VariantStockCountPersistenceMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public VariantStockCount loadByVariantIdOrZero(
            final VariantId variantId,
            final VariantProductId productId) {
        final var jpaId = variantId.value();

        return this.repository.findById(jpaId)
                .map(this.mapper::toDomain)
                .orElseGet(() -> VariantStockCount.zero(variantId, productId));
    }

    @Override
    public Map<VariantId, VariantStockCount> loadAllByVariantIds(
            final Set<VariantId> variantIdSet) {
        if (variantIdSet.isEmpty()) {
            return Map.of();
        }

        final var jpVariantIdSet = variantIdSet.stream()
                .map(VariantId::value)
                .collect(Collectors.toUnmodifiableSet());

        return this.repository.findAllById(jpVariantIdSet)
                .stream()
                .map(this.mapper::toDomain)
                .collect(VariantStockCountPersistenceAdapter.COLLECTOR);
    }

    @Override
    public VariantStockCount initialize(
            final NewVariantStockCount newStockCount) {
        final var query = new Query(Criteria.where("_id").is(newStockCount.getVariantId().value()));
        final var update = new Update()
                .setOnInsert(VariantStockCountDocument.Fields.productId,
                        newStockCount.getProductId().value())
                .setOnInsert(VariantStockCountDocument.Fields.value, 0)
                .setOnInsert(VariantStockCountDocument.Fields.lastUpdatedTime, Instant.now());

        return this.upsertAndReturnDomain(query, update);
    }

    @Override
    public Map<VariantId, VariantStockCount> initializeAll(
            final Collection<NewVariantStockCount> newStockCountCollection) {
        if (newStockCountCollection.isEmpty()) {
            return Map.of();
        }

        final var initialized = new ArrayList<VariantStockCount>(newStockCountCollection.size());

        final var bulkOps = this.mongoTemplate.bulkOps(BulkMode.UNORDERED, VariantStockCountDocument.class);
        final var instantNow = Instant.now();

        for (final var newStockCount : newStockCountCollection) {
            final var query = new Query(Criteria.where("_id").is(newStockCount.getVariantId().value()));
            final var update = new Update()
                    .setOnInsert(VariantStockCountDocument.Fields.productId,
                            newStockCount.getProductId().value())
                    .setOnInsert(VariantStockCountDocument.Fields.value, 0)
                    .setOnInsert(VariantStockCountDocument.Fields.lastUpdatedTime, instantNow);
            bulkOps.upsert(query, update);

            final var variantZeroStockCount = VariantStockCount.zero(
                    newStockCount.getVariantId(),
                    newStockCount.getProductId());
            initialized.add(variantZeroStockCount);
        }
        bulkOps.execute();

        return initialized.stream()
                .collect(VariantStockCountPersistenceAdapter.COLLECTOR);
    }

    @Override
    public VariantStockCount update(
            final VariantStockCount stockCount) {
        final var query = new Query(Criteria.where("_id").is(stockCount.getVariantId().value()));
        final var update = new Update()
                .setOnInsert(VariantStockCountDocument.Fields.productId, stockCount.getProductId().value())
                .set(VariantStockCountDocument.Fields.value, stockCount.getValue().value())
                .set(VariantStockCountDocument.Fields.lastUpdatedTime, Instant.now());

        return this.upsertAndReturnDomain(query, update);
    }

    @Override
    public void updateAll(
            final Collection<VariantStockCount> stockCountCollection) {
        if (stockCountCollection.isEmpty()) {
            return;
        }

        final var bulkOps = this.mongoTemplate.bulkOps(BulkMode.UNORDERED, VariantStockCountDocument.class);
        final var instantNow = Instant.now();

        for (final var stockCount : stockCountCollection) {
            final var query = new Query(Criteria.where("_id").is(stockCount.getVariantId().value()));
            final var update = new Update()
                    .setOnInsert(VariantStockCountDocument.Fields.productId,
                            stockCount.getProductId().value())
                    .set(VariantStockCountDocument.Fields.value, stockCount.getValue().value())
                    .set(VariantStockCountDocument.Fields.lastUpdatedTime, instantNow);
            bulkOps.upsert(query, update);
        }

        bulkOps.execute();
    }

    @Override
    public void deleteByVariantId(
            final VariantId variantId) {
        final var jpaVariantId = variantId.value();
        this.repository.deleteById(jpaVariantId);
    }

    @Override
    public void deleteAllByVariantIds(
            final Collection<VariantId> variantIdCollection) {
        if (variantIdCollection.isEmpty()) {
            return;
        }

        final var jpaVariantIdCollection = variantIdCollection.stream()
                .map(VariantId::value)
                .toList();
        this.repository.deleteAllById(jpaVariantIdCollection);
    }

    private VariantStockCount upsertAndReturnDomain(
            final Query query,
            final Update update) {
        final var options = FindAndModifyOptions.options().returnNew(true).upsert(true);
        final var doc = this.mongoTemplate.findAndModify(query, update, options, VariantStockCountDocument.class);
        Objects.requireNonNull(doc, "find-and-modify with upsert must return a non-null document");

        return this.mapper.toDomain(doc);
    }
}
