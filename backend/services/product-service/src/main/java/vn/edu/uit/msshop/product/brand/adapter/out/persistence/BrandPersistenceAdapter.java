package vn.edu.uit.msshop.product.brand.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.out.persistence.mapper.BrandPersistenceMapper;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.CheckBrandExistsPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.CreateBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.DeleteBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadSoftDeletedBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.UpdateBrandPort;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.creation.NewBrand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

@Component
@RequiredArgsConstructor
public class BrandPersistenceAdapter
        implements LoadBrandPort,
        LoadSoftDeletedBrandPort,
        CheckBrandExistsPort,
        CreateBrandPort,
        UpdateBrandPort,
        DeleteBrandPort {
    private final BrandMongoRepository repository;
    private final BrandPersistenceMapper mapper;

    @Override
    public Optional<Brand> loadById(
            final BrandId id) {
        final var jpaId = id.value();
        return this.repository
                .findByIdAndDeletionTimeIsNull(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public Optional<Brand> loadSoftDeletedById(
            final BrandId id) {
        final var jpaId = id.value();
        return this.repository
                .findByIdAndDeletionTimeIsNotNull(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public boolean existsById(
            final BrandId id) {
        final var jpaId = id.value();
        return this.repository
                .existsByIdAndDeletionTimeIsNull(jpaId);
    }

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
