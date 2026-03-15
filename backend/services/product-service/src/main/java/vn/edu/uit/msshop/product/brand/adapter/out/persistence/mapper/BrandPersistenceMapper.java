package vn.edu.uit.msshop.product.brand.adapter.out.persistence.mapper;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.brand.adapter.out.persistence.BrandDocument;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoKey;
import vn.edu.uit.msshop.product.brand.domain.model.BrandName;
import vn.edu.uit.msshop.product.brand.domain.model.BrandVersion;

@Component
public class BrandPersistenceMapper {
    public Brand toDomain(
            final BrandDocument entity) {
        final var id = new BrandId(entity.getId());
        final var name = new BrandName(entity.getName());
        final var logoKey = BrandLogoKey.ofNullable(entity.getLogoKey());

        final var version = BrandVersion.ofNullable(entity.getVersion());

        return new Brand(id, name, logoKey, version);
    }

    public BrandDocument toPersistence(
            final Brand brand) {
        return new BrandDocument(
                brand.getId().value(),
                brand.getName().value(),
                BrandLogoKey.unwrap(brand.getLogoKey()),
                BrandVersion.unwrap(brand.getVersion()));
    }
}
