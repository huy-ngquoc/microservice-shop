package vn.edu.uit.msshop.product.brand.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.out.persistence.mapper.BrandEntityMapper;
import vn.edu.uit.msshop.product.brand.application.port.out.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.SaveBrandPort;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.BrandId;

@Component
@RequiredArgsConstructor
public class BrandPersistenceAdapter
        implements LoadBrandPort, SaveBrandPort {
    private final SpringDataBrandJpaRepository repository;
    private final BrandEntityMapper mapper;

    @Override
    public Optional<Brand> loadById(
            final BrandId id) {
        final var jpaId = id.value();
        return this.repository.findById(jpaId).map(this.mapper::toDomain);
    }

    @Override
    public Brand save(
            final Brand brand) {
        final var toSave = this.mapper.toEntity(brand);
        final var saved = this.repository.save(toSave);
        return this.mapper.toDomain(saved);
    }
}
