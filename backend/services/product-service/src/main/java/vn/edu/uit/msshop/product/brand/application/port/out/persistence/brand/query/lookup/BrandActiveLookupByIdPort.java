package vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.lookup;

import java.util.Optional;

import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

public interface BrandActiveLookupByIdPort {
    Optional<Brand> loadById(
            final BrandId id);
}
