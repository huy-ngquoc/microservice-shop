package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.shared.adapter.out.persistence.PageRequests;
import vn.edu.uit.msshop.product.shared.application.dto.response.PageResponseDto;
import vn.edu.uit.msshop.product.variant.adapter.out.persistence.mapper.VariantPersistenceMapper;
import vn.edu.uit.msshop.product.variant.application.dto.query.ListVariantsQuery;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.CreateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.CreateVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.DeleteVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.DeleteVariantsForProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.ListVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllSoftDeletedVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadSoftDeletedVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantsForProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.SaveVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantsProductNameForProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateVariantPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductName;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTarget;

@Component
@RequiredArgsConstructor
public class VariantPersistenceAdapter
        implements
        ListVariantsPort,
        LoadVariantPort,
        LoadSoftDeletedVariantPort,
        LoadAllVariantsPort,
        LoadVariantsForProductPort,
        LoadAllSoftDeletedVariantsPort,
        CreateVariantPort,
        CreateAllVariantsPort,
        UpdateVariantPort,
        UpdateAllVariantsPort,
        UpdateAllVariantsProductNameForProductPort,
        DeleteVariantPort,
        DeleteVariantsForProductPort, SaveVariantPort {
    private final VariantMongoRepository repository;
    private final VariantPersistenceMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public PageResponseDto<Variant> list(
            final ListVariantsQuery query) {
        final var pageable = PageRequests.toPageable(
                query.pageRequest(),
                VariantDocument.Fields.id);

        final Page<VariantDocument> page;
        if (query.targets().isEmpty()) {
            page = this.repository.findAllByDeletionTimeIsNull(pageable);
        } else {
            final var rawTargets = query.targets().stream()
                    .map(VariantTarget::value)
                    .toList();
            page = this.repository.findAllByTargetsInAndDeletionTimeIsNull(rawTargets, pageable);
        }

        final var variants = page.getContent().stream()
                .map(this.mapper::toDomain)
                .toList();

        return new PageResponseDto<>(
                variants,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }

    @Override
    public Optional<Variant> loadById(
            final VariantId id) {
        final var jpaId = id.value();
        return this.repository
                .findByIdAndDeletionTimeIsNull(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public Optional<Variant> loadSoftDeletedById(
            final VariantId id) {
        final var jpaId = id.value();
        return this.repository
                .findByIdAndDeletionTimeIsNotNull(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public List<Variant> loadAllByIds(
            final Collection<VariantId> ids) {
        final var jpaIds = ids.stream()
                .map(VariantId::value)
                .toList();
        return this.repository.findAllByIdAndDeletionTimeIsNull(jpaIds).stream()
                .map(this.mapper::toDomain)
                .toList();
    }

    @Override
    public List<Variant> loadAllByProductId(
            final VariantProductId productId) {
        final var jpaProductId = productId.value();
        return this.repository.findAllByProductId(jpaProductId).stream()
                .map(this.mapper::toDomain)
                .toList();
    }

    @Override
    public List<Variant> loadAllSoftDeletedByIds(
            final Collection<VariantId> ids) {
        final var jpaIds = ids.stream()
                .map(VariantId::value)
                .toList();
        return this.repository.findAllByIdAndDeletionTimeIsNotNull(jpaIds).stream()
                .map(this.mapper::toDomain)
                .toList();
    }

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
        final var saved = this.repository.save(toSave);
        return this.mapper.toDomain(saved);
    }

    @Override
    public List<Variant> updateAll(
            final Collection<Variant> variants) {
        final var toSave = variants.stream()
                .map(this.mapper::toPersistence)
                .toList();
        final var saved = this.repository.saveAll(toSave);
        return saved.stream()
                .map(this.mapper::toDomain)
                .toList();
    }

    @Override
    public void updateProductNameByProductId(
            final VariantProductId productId,
            final VariantProductName productName) {
        final var query = Query.query(Criteria.where(VariantDocument.Fields.productId).is(productId.value()));
        final var update = Update.update(VariantDocument.Fields.productName, productName.value());
        this.mongoTemplate.updateMulti(
                query,
                update,
                VariantDocument.class);
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

    @Override
    public List<Variant> loadByListIds(List<VariantId> ids) {
        final var jpaVariantIds = ids.stream().map(VariantId::value).toList();
        System.out.println("First id "+jpaVariantIds.get(0).toString());
        // TODO Auto-generated method stub
        return this.repository.findByIdIn(jpaVariantIds).stream().map(mapper::toDomain).toList();
    }

    @Override
    @NonNull
    public Variant save(Variant v) {
        final var result = this.repository.save(this.mapper.toPersistence(v));
        return mapper.toDomain(result);
    }

    @Override
    @NonNull
    public List<Variant> saveAll(List<Variant> variants) {
        final var result = this.repository.saveAll(variants.stream().map(mapper::toPersistence).toList());
        return result.stream().map(mapper::toDomain).toList();
    }
}
