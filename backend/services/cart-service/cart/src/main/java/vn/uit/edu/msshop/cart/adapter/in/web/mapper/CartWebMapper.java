package vn.uit.edu.msshop.cart.adapter.in.web.mapper;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.adapter.in.web.request.CreateCartRequest;
import vn.uit.edu.msshop.cart.adapter.in.web.request.UpdateCartAmountReuest;
import vn.uit.edu.msshop.cart.adapter.in.web.request.common.ChangeRequest;
import vn.uit.edu.msshop.cart.adapter.in.web.response.CartDetailResponse;
import vn.uit.edu.msshop.cart.adapter.in.web.response.CartResponse;
import vn.uit.edu.msshop.cart.application.dto.command.ClearCartCommand;
import vn.uit.edu.msshop.cart.application.dto.command.CreateCartCommand;
import vn.uit.edu.msshop.cart.application.dto.command.DeleteCartItemCommand;
import vn.uit.edu.msshop.cart.application.dto.command.UpdateCartAmountCommand;
import vn.uit.edu.msshop.cart.application.dto.query.CartDetailView;
import vn.uit.edu.msshop.cart.application.dto.query.CartView;
import vn.uit.edu.msshop.cart.application.port.out.LoadVariantPort;
import vn.uit.edu.msshop.cart.domain.model.CartDetail;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Amount;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;

@Component
@RequiredArgsConstructor
public class CartWebMapper {
    private final LoadVariantPort loadPort;
    public CreateCartCommand toCommand(CreateCartRequest request) {
        List<CartDetail> listCartDetails = loadPort.loadCartDetails(request.getDetailRequests());
        return new CreateCartCommand(new UserId(request.getUserId()),listCartDetails);
    }
    public UpdateCartAmountCommand toCommand(UpdateCartAmountReuest request) {
        return new UpdateCartAmountCommand(new UserId(request.getUserId()), new VariantId(request.getVariantId()),ChangeRequest.toChange(request.getAmount(), Amount::new));
    }
    public CartResponse toResponse(CartView view) {
        List<CartDetailResponse> listDetailResponses = view.getDetailViews().stream().map(this::toDetailResponse).toList();
        return new CartResponse(view.getUserId(), listDetailResponses);
    } 
    public CartDetailResponse toDetailResponse(CartDetailView view) {
        return new CartDetailResponse(view.getVariantId(), view.getName(), view.getImageUrls(), view.getColor(), view.getSize(), view.getPrice(), view.getAmount());
    }
    public ClearCartCommand toCommand(String userId) {
        return new ClearCartCommand(new UserId(UUID.fromString(userId)));
    }
    public DeleteCartItemCommand toCommand(String userId, String variantId) {
        return new DeleteCartItemCommand(new UserId(UUID.fromString(userId)), new VariantId(UUID.fromString(variantId)));
    }
}
