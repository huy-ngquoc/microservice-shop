package vn.edu.uit.msshop.product.brand.application.port.out.persistence;

import java.util.Optional;

import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

public interface LoadSoftDeletedBrandPort {
    Optional<Brand> loadSoftDeletedById(
            final BrandId id);
}
