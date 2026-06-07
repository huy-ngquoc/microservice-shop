package vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.command;

import vn.edu.uit.msshop.product.brand.domain.model.Brand;

public interface BrandUpdatePort {
    Brand update(
            final Brand brand);
}
