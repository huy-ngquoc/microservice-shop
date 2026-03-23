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
import vn.uit.edu.msshop.cart.application.common.Change;
import vn.uit.edu.msshop.cart.application.dto.command.ClearCartCommand;
import vn.uit.edu.msshop.cart.application.dto.command.CreateCartCommand;
import vn.uit.edu.msshop.cart.application.dto.command.DeleteCartItemCommand;
import vn.uit.edu.msshop.cart.application.dto.command.UpdateCartAmountCommand;
import vn.uit.edu.msshop.cart.application.dto.command.UpdateCartInfoCommand;
import vn.uit.edu.msshop.cart.application.dto.query.CartDetailView;
import vn.uit.edu.msshop.cart.application.dto.query.CartView;
import vn.uit.edu.msshop.cart.application.port.out.LoadVariantPort;
import vn.uit.edu.msshop.cart.domain.event.OrderCreatedSuccess;
import vn.uit.edu.msshop.cart.domain.event.ProductUpdated;
import vn.uit.edu.msshop.cart.domain.model.CartDetail;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Amount;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Color;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ImageUrls;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ProductName;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Size;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UnitPrice;
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
        return new CartDetailResponse(view.getVariantId(), view.getName(), view.getImageUrls(), view.getColor(), view.getSize(), view.getPrice(), view.getAmount(), view.getInventoryAmount());
    }
    public ClearCartCommand toCommand(String userId) {
        return new ClearCartCommand(new UserId(UUID.fromString(userId)));
    }
    public DeleteCartItemCommand toCommand(String userId, String variantId) {
        return new DeleteCartItemCommand(new UserId(UUID.fromString(userId)), new VariantId(UUID.fromString(variantId)));
    }
    public List<DeleteCartItemCommand> toCommand(OrderCreatedSuccess event) {
        return event.variantIds().stream().map(item->new DeleteCartItemCommand(new UserId(event.userId()),new VariantId(item))).toList();
    }
    public UpdateCartInfoCommand toCommand(ProductUpdated event, String userId) {
        final var color = Change.set(new Color(event.getColor()));
        final var imageUrls = Change.set(new ImageUrls(event.getImageUrls()));
        final var name = Change.set(new ProductName(event.getName()));
        final var size = Change.set(new Size(event.getSize()));
        final var unitPrice = Change.set(new UnitPrice(event.getUnitPrice()));

        return new UpdateCartInfoCommand(new UserId(UUID.fromString(userId)), new VariantId(event.getVariantId()), color, imageUrls, name, size, unitPrice);
    }
}
