package vn.uit.edu.msshop.cart.adapter.remote;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import vn.uit.edu.msshop.cart.adapter.in.web.request.CreateCartItemRequest;
import vn.uit.edu.msshop.cart.application.port.out.LoadVariantPort;
import vn.uit.edu.msshop.cart.domain.model.CartDetail;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Amount;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Color;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ImageUrls;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ProductName;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Size;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UnitPrice;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;

@Service
public class LoadVariantService implements LoadVariantPort {

    @Override
    public List<CartDetail> loadCartDetails(List<CreateCartItemRequest> detailRequest) {
        List<CartDetail> result= new ArrayList<>();
        for(CreateCartItemRequest req: detailRequest) {
            final var d = CartDetail.Draft.builder().variantId(new VariantId(req.getVariantId()))
            .imageUrls(new ImageUrls(new ArrayList<>()))
            .name(new ProductName("Product"))
            .color(new Color("Red"))
            .size(new Size("XS"))
            .amount(new Amount(req.getAmount()))
            .price(new UnitPrice(10000)).build();

            CartDetail cd = CartDetail.create(d);
            result.add(cd);
        }
        return result;
    }

}
