package vn.edu.uit.msshop.product.application.port.out;

import java.util.Optional;

import vn.edu.uit.msshop.product.domain.model.brand.Brand;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;

public interface LoadBrandPort {
    Optional<Brand> loadById(
            final BrandId id);
}
