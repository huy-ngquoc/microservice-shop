package vn.edu.uit.msshop.product.brand.application.port.out.persistence;

import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.creation.NewBrand;

public interface CreateBrandPort {
  Brand create(final NewBrand newBrand);
}
