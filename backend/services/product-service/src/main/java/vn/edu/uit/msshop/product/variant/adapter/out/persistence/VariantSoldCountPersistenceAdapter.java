package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.out.persistence.mapper.VariantSoldCountPersistenceMapper;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.DeleteAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.DeleteVariantSoldCountPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.InitializeAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.InitializeVariantSoldCountPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantSoldCountPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateVariantSoldCountPort;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Component
@RequiredArgsConstructor
public class VariantSoldCountPersistenceAdapter
        implements
        LoadVariantSoldCountPort,
        LoadAllVariantSoldCountsPort,
        InitializeVariantSoldCountPort,
        InitializeAllVariantSoldCountsPort,
        UpdateVariantSoldCountPort,
        UpdateAllVariantSoldCountsPort,
        DeleteVariantSoldCountPort,
        DeleteAllVariantSoldCountsPort {
    private static final Collector<
            VariantSoldCount,
            ?,
            Map<VariantId, VariantSoldCount>> COLLECTOR = Collectors.toUnmodifiableMap(
                    VariantSoldCount::getId,
                    Function.identity(),
                    (
                            existing,
                            replacement) -> existing);

    private final VariantSoldCountMongoRepository repository;
    private final VariantSoldCountPersistenceMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<VariantSoldCount> loadById(
            final VariantId id) {
        final var jpaId = id.value();
        return this.repository.findById(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public Map<VariantId, VariantSoldCount> loadAllByIds(
            final Set<VariantId> ids) {
        if (ids.isEmpty()) {
            return Map.of();
        }

        final var jpaIds = ids.stream()
                .map(VariantId::value)
                .toList();
        return this.repository.findAllById(jpaIds).stream()
                .map(this.mapper::toDomain)
                .collect(VariantSoldCountPersistenceAdapter.COLLECTOR);
    }

    @Override
    public VariantSoldCount initialize(
            final NewVariantSoldCount newSoldCount) {
        final var query = new Query(Criteria.where("_id").is(newSoldCount.getVariantId().value()));
        final var update = new Update()
                .setOnInsert(VariantSoldCountDocument.Fields.productId, newSoldCount.getProductId().value())
                .setOnInsert(VariantSoldCountDocument.Fields.value, 0)
                .setOnInsert(VariantSoldCountDocument.Fields.lastUpdatedTime, Instant.now());

        return this.upsertAndReturnDomain(query, update);
    }

    @Override
    public Map<VariantId, VariantSoldCount> initializeAll(
            Collection<NewVariantSoldCount> newSoldCounts) {
        if (newSoldCounts.isEmpty()) {
            return Map.of();
        }

        final var initialized = new ArrayList<VariantSoldCount>(newSoldCounts.size());

        final var bulk = this.mongoTemplate.bulkOps(
                BulkMode.UNORDERED,
                VariantSoldCountDocument.class);
        final var now = Instant.now();
        for (final var newSoldCount : newSoldCounts) {
            final var query = new Query(Criteria.where("_id").is(newSoldCount.getVariantId().value()));
            final var update = new Update()
                    .setOnInsert(VariantSoldCountDocument.Fields.productId,
                            newSoldCount.getProductId().value())
                    .setOnInsert(VariantSoldCountDocument.Fields.value, 0)
                    .setOnInsert(VariantSoldCountDocument.Fields.lastUpdatedTime, now);
            bulk.upsert(query, update);

            initialized.add(VariantSoldCount.zero(
                    newSoldCount.getVariantId(),
                    newSoldCount.getProductId()));
        }
        bulk.execute();

        return initialized.stream()
                .collect(VariantSoldCountPersistenceAdapter.COLLECTOR);
    }

    @Override
    public VariantSoldCount update(
            final VariantSoldCount soldCount) {
        final var query = new Query(Criteria.where("_id").is(soldCount.getId().value()));
        final var update = new Update()
                .setOnInsert(VariantSoldCountDocument.Fields.productId, soldCount.getProductId().value())
                .set(VariantSoldCountDocument.Fields.value, soldCount.getValue().value())
                .set(VariantSoldCountDocument.Fields.lastUpdatedTime, Instant.now());

        return this.upsertAndReturnDomain(query, update);
    }

    @Override
    public void updateAll(
            Collection<VariantSoldCount> soldCounts) {
        if (soldCounts.isEmpty()) {
            return;
        }

        final var ops = this.mongoTemplate.bulkOps(
                BulkOperations.BulkMode.UNORDERED,
                VariantSoldCountDocument.class);
        final var instantNow = Instant.now();

        for (final var soldCount : soldCounts) {
            final var query = new Query(Criteria.where("_id").is(soldCount.getId().value()));
            final var update = new Update()
                    .setOnInsert(VariantSoldCountDocument.Fields.productId, soldCount.getProductId().value())
                    .set(VariantSoldCountDocument.Fields.value, soldCount.getValue().value())
                    .set(VariantSoldCountDocument.Fields.lastUpdatedTime, instantNow);
            ops.upsert(query, update);
        }

        ops.execute();
    }

    @Override
    public void deleteById(
            final VariantId id) {
        final var jpaId = id.value();
        this.repository.deleteById(jpaId);
    }

    @Override
    public void deleteAllByIds(
            final Collection<VariantId> ids) {
        if (ids.isEmpty()) {
            return;
        }

        final var jpaIds = ids.stream()
                .map(VariantId::value)
                .toList();
        this.repository.deleteAllById(jpaIds);
    }

    private VariantSoldCount upsertAndReturnDomain(
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
                VariantSoldCountDocument.class);
        Objects.requireNonNull(doc, "find-and-modify with upsert must return a non-null document");

        return this.mapper.toDomain(doc);
    }
}
