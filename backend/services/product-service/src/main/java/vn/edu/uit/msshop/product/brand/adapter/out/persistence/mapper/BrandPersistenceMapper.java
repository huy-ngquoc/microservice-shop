package vn.edu.uit.msshop.product.brand.adapter.out.persistence.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.brand.adapter.out.persistence.BrandDocument;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.creation.NewBrand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandDeletionTime;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandName;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;

@Component
public class BrandPersistenceMapper {
    public Brand toDomain(
            final BrandDocument entity) {
        final var id = new BrandId(entity.getId());
        final var name = new BrandName(entity.getName());
        final var logoKey = BrandLogoKey.ofNullable(entity.getLogoKey());

        final var versionValue = Objects.requireNonNull(
                entity.getVersion(),
                "Persisted brand must have a version");
        final var version = new BrandVersion(versionValue);
        final var deletionTime = BrandDeletionTime.ofNullable(entity.getDeletionTime());

        return new Brand(
                id,
                name,
                logoKey,
                version,
                deletionTime);
    }

    public BrandDocument toPersistence(
            final Brand brand) {
        return new BrandDocument(
                brand.getId().value(),
                brand.getName().value(),
                BrandLogoKey.unwrap(brand.getLogoKey()),
                brand.getVersion().value(),
                BrandDeletionTime.unwrap(brand.getDeletionTime()));
    }

    public BrandDocument toPersistence(
            final NewBrand newBrand) {
        return new BrandDocument(
                newBrand.getId().value(),
                newBrand.getName().value(),
                null,
                null,
                null);
    }
}
