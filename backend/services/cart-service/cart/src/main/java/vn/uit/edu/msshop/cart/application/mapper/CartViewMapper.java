package vn.uit.edu.msshop.cart.application.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.adapter.in.web.response.InventoryResponse;
import vn.uit.edu.msshop.cart.adapter.remote.LoadInventoryService;
import vn.uit.edu.msshop.cart.application.dto.query.CartDetailView;
import vn.uit.edu.msshop.cart.application.dto.query.CartView;
import vn.uit.edu.msshop.cart.domain.model.Cart;
import vn.uit.edu.msshop.cart.domain.model.CartDetail;

@Component
@RequiredArgsConstructor
public class CartViewMapper {
    private final LoadInventoryService loadInventoryService;
    public CartDetailView toView(CartDetail cartDetail, int inventoryAmount) {
        return new CartDetailView(cartDetail.getVariantId().value(), cartDetail.getImageUrls().images(), cartDetail.getColor().value(), cartDetail.getName().value(), cartDetail.getSize().value(), cartDetail.getPrice().value(), cartDetail.getAmount().value(),inventoryAmount);
    }
    public CartView toView(Cart cart) {
        CartView result=  new CartView(cart.getUserId().value(),new ArrayList<>());
        List<UUID> variantIds = cart.getDetails().stream().map(item->item.getVariantId().value()).toList();
        List<InventoryResponse> inventoryResponses = loadInventoryService.getInventoryBatch(variantIds);
        System.out.println(inventoryResponses.size());
        for(CartDetail detail:cart.getDetails()) {
            InventoryResponse i = findByVariantIdInList(inventoryResponses, detail.getVariantId().value());
            int inventoryAmount = i==null?0:i.getQuantity();
            result.getDetailViews().add(toView(detail,inventoryAmount));
        }
        return result;
    }
    private InventoryResponse findByVariantIdInList(List<InventoryResponse> responses, UUID variantId) {
        for(InventoryResponse response: responses) {
            if(response.getVariantId().equals(variantId)) {
                return response;
            }
        }
        return null;
    }
}
