package vn.edu.uit.msshop.product.brand.application.port.out;

import java.util.Optional;

import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.BrandId;

public interface LoadBrandPort {
    Optional<Brand> loadById(
            final BrandId id);
}
