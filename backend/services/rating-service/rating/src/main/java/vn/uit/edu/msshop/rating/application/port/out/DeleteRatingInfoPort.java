package vn.uit.edu.msshop.rating.application.port.out;

import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;


public interface DeleteRatingInfoPort {
    public void delete(ProductId productId);
}
