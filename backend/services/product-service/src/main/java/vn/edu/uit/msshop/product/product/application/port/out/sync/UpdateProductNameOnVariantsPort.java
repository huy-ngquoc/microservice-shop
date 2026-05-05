package vn.edu.uit.msshop.product.product.application.port.out.sync;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;

public interface UpdateProductNameOnVariantsPort {
  void updateProductNameByProductId(final ProductId productId, final ProductName productName);
}
