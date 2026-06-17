package vn.edu.uit.msshop.product.variant.adapter.out.persistence.count;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
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
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantSoldCountBulkDeletionByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantSoldCountBulkInitializationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantSoldCountBulkUpdatePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantSoldCountDeletionByVariantIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantSoldCountInitializationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantSoldCountUpdatePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantSoldCountBulkLookupByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantSoldCountLookupByVariantIdPort;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Component
@RequiredArgsConstructor
public class VariantSoldCountPersistenceAdapter
        implements
        VariantSoldCountLookupByVariantIdPort,
        VariantSoldCountBulkLookupByVariantIdsPort,
        VariantSoldCountInitializationPort,
        VariantSoldCountBulkInitializationPort,
        VariantSoldCountUpdatePort,
        VariantSoldCountBulkUpdatePort,
        VariantSoldCountDeletionByVariantIdPort,
        VariantSoldCountBulkDeletionByVariantIdsPort {

    private final VariantSoldCountMongoRepository repository;
    private final VariantSoldCountPersistenceMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<VariantSoldCount> loadByVariantId(
            final VariantId variantId) {
        final var jpaId = variantId.value();
        return this.repository.findById(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public Map<VariantId, VariantSoldCount> loadAllByVariantIds(
            final Set<VariantId> variantIdSet) {
        if (variantIdSet.isEmpty()) {
            return Map.of();
        }

        final var jpaVariantIdSet = variantIdSet.stream()
                .map(VariantId::value)
                .toList();
        final var docList = this.repository.findAllById(jpaVariantIdSet);

        return docList.stream()
                .map(this.mapper::toDomain)
                .collect(Collectors.toUnmodifiableMap(
                        VariantSoldCount::getVariantId,
                        Function.identity()));
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
            Collection<NewVariantSoldCount> newSoldCountCollection) {
        if (newSoldCountCollection.isEmpty()) {
            return Map.of();
        }

        final var initialized = new ArrayList<VariantSoldCount>(newSoldCountCollection.size());

        final var bulk = this.mongoTemplate.bulkOps(
                BulkMode.UNORDERED,
                VariantSoldCountDocument.class);
        final var now = Instant.now();
        for (final var newSoldCount : newSoldCountCollection) {
            final var query = new Query(Criteria.where("_id").is(newSoldCount.getVariantId().value()));
            final var update = new Update()
                    .setOnInsert(VariantSoldCountDocument.Fields.productId,
                            newSoldCount.getProductId().value())
                    .setOnInsert(VariantSoldCountDocument.Fields.value, 0)
                    .setOnInsert(VariantSoldCountDocument.Fields.lastUpdatedTime, now);
            bulk.upsert(query, update);

            final var variantZeroSoldCount = VariantSoldCount.zero(
                    newSoldCount.getVariantId(),
                    newSoldCount.getProductId());
            initialized.add(variantZeroSoldCount);
        }
        bulk.execute();

        return initialized.stream()
                .collect(Collectors.toUnmodifiableMap(
                        VariantSoldCount::getVariantId,
                        Function.identity()));
    }

    @Override
    public VariantSoldCount update(
            final VariantSoldCount soldCount) {
        final var query = new Query(Criteria.where("_id").is(soldCount.getVariantId().value()));
        final var update = new Update()
                .setOnInsert(VariantSoldCountDocument.Fields.productId, soldCount.getProductId().value())
                .set(VariantSoldCountDocument.Fields.value, soldCount.getValue().value())
                .set(VariantSoldCountDocument.Fields.lastUpdatedTime, Instant.now());

        return this.upsertAndReturnDomain(query, update);
    }

    @Override
    public void updateAll(
            Collection<VariantSoldCount> soldCountCollection) {
        if (soldCountCollection.isEmpty()) {
            return;
        }

        final var ops = this.mongoTemplate.bulkOps(
                BulkOperations.BulkMode.UNORDERED,
                VariantSoldCountDocument.class);
        final var instantNow = Instant.now();

        for (final var soldCount : soldCountCollection) {
            final var query = new Query(Criteria.where("_id").is(soldCount.getVariantId().value()));
            final var update = new Update()
                    .setOnInsert(VariantSoldCountDocument.Fields.productId, soldCount.getProductId().value())
                    .set(VariantSoldCountDocument.Fields.value, soldCount.getValue().value())
                    .set(VariantSoldCountDocument.Fields.lastUpdatedTime, instantNow);
            ops.upsert(query, update);
        }

        ops.execute();
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

        final var jpaVariantIdList = variantIdCollection.stream()
                .map(VariantId::value)
                .toList();
        this.repository.deleteAllById(jpaVariantIdList);
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
