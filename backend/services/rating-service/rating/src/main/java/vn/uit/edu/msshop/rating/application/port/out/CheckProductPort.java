package vn.uit.edu.msshop.rating.application.port.out;

import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;

public interface CheckProductPort {
    public boolean isProductExist(ProductId productId);
}
