package vn.edu.uit.msshop.product.brand.application.port.out;

import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.NewBrand;

public interface CreateBrandPort {
    Brand create(
            final NewBrand newBrand);
}
