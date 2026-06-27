package vn.edu.uit.msshop.product.variant.adapter.out.persistence.variant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantBulkCreationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantBulkDeletionByProductIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantBulkUpdatePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantCreationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantDeletionByIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantProductNameBulkUpdateByProductIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantUpdatePort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductName;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Component
@RequiredArgsConstructor
public class VariantCommandPersistenceAdapter
        implements
        VariantCreationPort,
        VariantBulkCreationPort,
        VariantUpdatePort,
        VariantBulkUpdatePort,
        VariantProductNameBulkUpdateByProductIdPort,
        VariantDeletionByIdPort,
        VariantBulkDeletionByProductIdPort {

    private final VariantMongoRepository repository;
    private final VariantPersistenceMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public Variant create(
            final NewVariant newVariant) {
        final var toSave = this.mapper.toPersistence(newVariant);
        final var saved = this.repository.save(toSave);
        return this.mapper.toDomain(saved);
    }

    @Override
    public List<Variant> createAll(
            final Collection<NewVariant> newVariants) {
        final var toSave = newVariants.stream()
                .map(this.mapper::toPersistence)
                .toList();
        final var saved = this.repository.saveAll(toSave);
        return saved.stream()
                .map(this.mapper::toDomain)
                .toList();
    }

    @Override
    public Variant update(
            final Variant variant) {
        final var toSave = this.mapper.toPersistence(variant);
        final VariantDocument saved;
        try {
            saved = this.repository.save(toSave);
        } catch (final OptimisticLockingFailureException _) {
            final var expected = toSave.getVersion();
            final var current = this.repository.findById(toSave.getId())
                    .map(VariantDocument::getVersion)
                    .orElse(null);
            throw new OptimisticLockException(expected, current);
        }
        return this.mapper.toDomain(saved);
    }

    @Override
    public List<Variant> updateAll(
            final Collection<Variant> variants) {
        if (variants.isEmpty()) {
            return List.of();
        }

        final var toSaveAll = variants.stream()
                .map(this.mapper::toPersistence)
                .toList();
        final var savedAll = new ArrayList<VariantDocument>(toSaveAll.size());
        for (final var toSave : toSaveAll) {
            try {
                savedAll.add(this.repository.save(toSave));
            } catch (final OptimisticLockingFailureException _) {
                final var expected = toSave.getVersion();
                final var current = this.repository.findById(toSave.getId())
                        .map(VariantDocument::getVersion)
                        .orElse(null);
                throw new OptimisticLockException(expected, current);
            }
        }
        return savedAll.stream()
                .map(this.mapper::toDomain)
                .toList();
    }

    @Override
    public void updateProductNameByProductId(
            final VariantProductId productId,
            final VariantProductName productName) {
        final var query = Query.query(Criteria.where(VariantDocument.Fields.productId).is(productId.value()));
        final var update = Update.update(VariantDocument.Fields.productName, productName.value());
        this.mongoTemplate.updateMulti(query, update, VariantDocument.class);
    }

    @Override
    public void deleteById(
            final VariantId id) {
        // TODO: variable name "jpaId" is suitable?
        final var jpaId = id.value();
        this.repository.deleteById(jpaId);
    }

    @Override
    public void deleteByProductId(
            final VariantProductId productId) {
        final var jpaProductId = productId.value();
        this.repository.deleteAllByProductId(jpaProductId);
    }
}
