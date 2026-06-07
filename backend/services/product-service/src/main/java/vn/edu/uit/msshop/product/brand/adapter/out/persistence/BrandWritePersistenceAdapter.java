package vn.edu.uit.msshop.product.brand.adapter.out.persistence;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.out.persistence.mapper.BrandPersistenceMapper;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.command.BrandCreationPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.command.BrandDeletionByIdPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.command.BrandUpdatePort;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.creation.NewBrand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

@Component
@RequiredArgsConstructor
public class BrandWritePersistenceAdapter
        implements
        BrandCreationPort,
        BrandUpdatePort,
        BrandDeletionByIdPort {

    private final BrandMongoRepository repository;
    private final BrandPersistenceMapper mapper;

    @Override
    public Brand create(
            final NewBrand newBrand) {
        final var toSave = this.mapper.toPersistence(newBrand);
        final var saved = this.repository.save(toSave);
        return this.mapper.toDomain(saved);
    }

    @Override
    public Brand update(
            final Brand brand) {
        final var toSave = this.mapper.toPersistence(brand);
        final var saved = this.repository.save(toSave);
        return this.mapper.toDomain(saved);
    }

    @Override
    public void deleteById(
            final BrandId id) {
        final var jpaId = id.value();
        this.repository.deleteById(jpaId);
    }
}
