package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.out.persistence.mapper.VariantPersistenceMapper;
import vn.edu.uit.msshop.product.variant.application.port.out.CreateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.CreateVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.LoadAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.LoadVariantsForProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.UpdateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.UpdateVariantPort;
import vn.edu.uit.msshop.product.variant.domain.model.NewVariant;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;

@Component
@RequiredArgsConstructor
public class VariantPersistenceAdapter
        implements LoadVariantPort,
        LoadAllVariantsPort,
        LoadVariantsForProductPort,
        CreateVariantPort,
        CreateAllVariantsPort,
        UpdateVariantPort,
        UpdateAllVariantsPort {
    private final VariantMongoRepository repository;
    private final VariantPersistenceMapper mapper;

    @Override
    public Optional<Variant> loadById(
            final VariantId id) {
        final var jpaId = id.value();
        return this.repository.findById(jpaId).map(this.mapper::toDomain);
    }

    @Override
    public List<Variant> loadAllByIds(
            final Collection<VariantId> ids) {
        final var jpaIds = ids.stream()
                .map(VariantId::value)
                .toList();
        return this.repository.findAllById(jpaIds).stream()
                .map(this.mapper::toDomain)
                .toList();
    }

    @Override
    public List<Variant> loadByProductId(
            final VariantProductId productId) {
        final var jpaProductId = productId.value();
        return this.repository.findByProductId(jpaProductId).stream()
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
}
