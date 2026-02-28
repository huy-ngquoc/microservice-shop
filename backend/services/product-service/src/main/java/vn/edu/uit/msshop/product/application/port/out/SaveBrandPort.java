package vn.edu.uit.msshop.product.application.port.out;

import vn.edu.uit.msshop.product.domain.model.brand.Brand;

public interface SaveBrandPort {
    Brand save(
            final Brand brand);
}
