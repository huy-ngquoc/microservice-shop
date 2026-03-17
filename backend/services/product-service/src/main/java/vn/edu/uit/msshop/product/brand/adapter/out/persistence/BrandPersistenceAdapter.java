package vn.edu.uit.msshop.product.brand.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.out.persistence.mapper.BrandPersistenceMapper;
import vn.edu.uit.msshop.product.brand.application.port.out.CreateBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.UpdateBrandPort;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.NewBrand;

@Component
@RequiredArgsConstructor
public class BrandPersistenceAdapter
        implements LoadBrandPort, CreateBrandPort, UpdateBrandPort {
    private final BrandMongoRepository repository;
    private final BrandPersistenceMapper mapper;

    @Override
    public Optional<Brand> loadById(
            final BrandId id) {
        final var jpaId = id.value();
        return this.repository.findById(jpaId).map(this.mapper::toDomain);
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
}
