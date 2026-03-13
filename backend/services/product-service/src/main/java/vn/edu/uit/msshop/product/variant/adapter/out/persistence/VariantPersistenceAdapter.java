package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.out.persistence.mapper.VariantPersistenceMapper;
import vn.edu.uit.msshop.product.variant.application.port.out.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.SaveVariantPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;

@Component
@RequiredArgsConstructor
public class VariantPersistenceAdapter
        implements LoadVariantPort, SaveVariantPort {
    private final VariantMongoRepository repository;
    private final VariantPersistenceMapper mapper;

    @Override
    public Optional<Variant> loadById(
            final VariantId id) {
        final var jpaId = id.value();
        return this.repository.findById(jpaId).map(this.mapper::toDomain);
    }

    @Override
    public Variant save(
            final Variant variant) {
        final var toSave = this.mapper.toPersistence(variant);
        final var saved = this.repository.save(toSave);
        return this.mapper.toDomain(saved);
    }
}
