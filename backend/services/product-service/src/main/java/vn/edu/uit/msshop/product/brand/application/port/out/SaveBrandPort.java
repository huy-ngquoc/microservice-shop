package vn.edu.uit.msshop.product.brand.application.port.out;

import vn.edu.uit.msshop.product.brand.domain.model.Brand;

public interface SaveBrandPort {
    Brand save(
            final Brand brand);
}
