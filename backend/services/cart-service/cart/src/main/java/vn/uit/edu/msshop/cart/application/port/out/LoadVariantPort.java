package vn.uit.edu.msshop.cart.application.port.out;

import java.util.List;

import vn.uit.edu.msshop.cart.adapter.in.web.request.CreateCartItemRequest;
import vn.uit.edu.msshop.cart.domain.model.CartDetail;

public interface LoadVariantPort {
    public List<CartDetail> loadCartDetails(
            List<CreateCartItemRequest> detailRequest);
}
