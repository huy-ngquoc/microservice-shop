package vn.uit.edu.msshop.cart.adapter.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.adapter.in.web.request.CreateCartItemRequest;
import vn.uit.edu.msshop.cart.adapter.in.web.request.FindVariantsByIdsRequest;
import vn.uit.edu.msshop.cart.adapter.in.web.response.VariantResponse;
import vn.uit.edu.msshop.cart.application.port.out.LoadVariantPort;
import vn.uit.edu.msshop.cart.domain.model.CartDetail;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Amount;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ImageKey;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ProductName;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UnitPrice;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantTraits;

@Service
@RequiredArgsConstructor
public class LoadVariantService implements LoadVariantPort {
    private final ProductCaller productCaller;
    @Override
    public List<CartDetail> loadCartDetails(
            List<CreateCartItemRequest> detailRequest) {
        List<CartDetail> result = new ArrayList<>();
        FindVariantsByIdsRequest requests = new FindVariantsByIdsRequest(new HashSet(detailRequest.stream().map(item->item.getVariantId()).toList()));
        List<VariantResponse> responses = productCaller.findAllByIds(requests).getBody();
        Map<UUID,VariantResponse> variantResponseMap = new HashMap<>(); 
        for(VariantResponse response:responses) {
            variantResponseMap.put(response.id(), response);
        }
        for (CreateCartItemRequest req : detailRequest) {
            VariantResponse response = variantResponseMap.get(req.getVariantId());
            if(response==null) throw new RuntimeException("Variant not exist");
            final var d = CartDetail.Draft.builder().variantId(new VariantId(req.getVariantId()))
                    .imageKey(new ImageKey(response.imageKey()))
                    .name(new ProductName(response.productName()))
                    .traits(new VariantTraits(response.traits()))
                    .amount(new Amount(req.getAmount()))
                    .price(new UnitPrice(response.price())).build();

            CartDetail cd = CartDetail.create(d);
            result.add(cd);
        }
        return result;
    }

}
