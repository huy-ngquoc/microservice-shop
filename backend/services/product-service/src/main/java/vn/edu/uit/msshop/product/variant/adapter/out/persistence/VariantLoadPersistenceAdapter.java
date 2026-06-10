package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllSoftDeletedVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadSoftDeletedVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantsForProductPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Component
@RequiredArgsConstructor
public class VariantLoadPersistenceAdapter
        implements
        LoadVariantPort,
        LoadSoftDeletedVariantPort,
        LoadAllVariantsPort,
        LoadVariantsForProductPort,
        LoadAllSoftDeletedVariantsPort {
    private final VariantMongoRepository repository;
    private final VariantPersistenceMapper mapper;

    @Override
    public Optional<Variant> loadById(
            final VariantId id) {
        final var jpaId = id.value();
        return this.repository.findByIdAndDeletionTimeIsNull(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public Optional<Variant> loadSoftDeletedById(
            final VariantId id) {
        final var jpaId = id.value();
        return this.repository.findByIdAndDeletionTimeIsNotNull(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public Map<VariantId, Variant> loadAllByIds(
            final Set<VariantId> ids) {
        final var jpaIds = ids.stream()
                .map(VariantId::value)
                .toList();

        final var docs = this.repository.findAllByIdInAndDeletionTimeIsNull(jpaIds);
        return this.mapper.toDomainMap(docs);
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
        return this.repository.findAllByIdInAndDeletionTimeIsNotNull(jpaIds).stream()
                .map(this.mapper::toDomain)
                .toList();
    }
}
