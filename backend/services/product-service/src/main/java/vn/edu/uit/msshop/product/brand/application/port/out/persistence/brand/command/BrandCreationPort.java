package vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.command;

import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.creation.NewBrand;

public interface BrandCreationPort {
    Brand create(
            final NewBrand newBrand);
}
