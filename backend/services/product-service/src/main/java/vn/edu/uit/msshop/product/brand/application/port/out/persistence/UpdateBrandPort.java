package vn.edu.uit.msshop.product.brand.application.port.out.persistence;

import vn.edu.uit.msshop.product.brand.domain.model.Brand;

public interface UpdateBrandPort {
  Brand update(final Brand brand);
}
