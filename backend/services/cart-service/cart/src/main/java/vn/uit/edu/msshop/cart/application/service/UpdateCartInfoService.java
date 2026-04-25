package vn.uit.edu.msshop.cart.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.CartApplication;
import vn.uit.edu.msshop.cart.application.dto.command.UpdateCartInfoCommand;
import vn.uit.edu.msshop.cart.application.dto.query.CartView;
import vn.uit.edu.msshop.cart.application.exception.CartNotFoundException;
import vn.uit.edu.msshop.cart.application.mapper.CartViewMapper;
import vn.uit.edu.msshop.cart.application.port.in.UpdateCartInfoUseCase;
import vn.uit.edu.msshop.cart.application.port.out.LoadCartPort;
import vn.uit.edu.msshop.cart.application.port.out.SaveCartPort;
import vn.uit.edu.msshop.cart.domain.model.Cart;
import vn.uit.edu.msshop.cart.domain.model.CartDetail;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Color;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ImageUrls;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ProductName;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Size;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UnitPrice;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;

@Service
@RequiredArgsConstructor
public class UpdateCartInfoService implements UpdateCartInfoUseCase {
    private final SaveCartPort saveCartPort;
    private final LoadCartPort loadCartPort;
    private final CartViewMapper mapper;

    @Override
    public CartView updateInfo(List<UpdateCartInfoCommand> commands) {
        if(commands.isEmpty()) return null;
        UserId userId= commands.get(0).userId();
        Cart cart = loadCartPort.loadByUserId(userId);
        if(cart==null) throw new CartNotFoundException(userId);
        List<CartDetail.UpdateInfo> updateDetailInfos = getUpdateDetailInfos(commands, cart);
        final var updateInfo= Cart.UpdateInfo.builder().userId(userId).detailUpdateInfos(updateDetailInfos).build();
        final var saved = cart.applyUpdateInfo(updateInfo);
        return mapper.toView(saveCartPort.save(saved));
        
    }
    private List<CartDetail.UpdateInfo> getUpdateDetailInfos(List<UpdateCartInfoCommand> commands, Cart cart) {

        return commands.stream().map(item->{
            CartDetail detail = cart.findByVariantId(item.variantId());
            if(detail==null) throw new IllegalArgumentException("Invalid argument");
            return CartDetail.UpdateInfo.builder().variantId(item.variantId())
            .imageKey(item.imageKey().apply(detail.getImageKey()))
            .name(item.name().apply(detail.getName()))
            .price(item.unitPrice().apply(detail.getPrice()))
            .build();
        }).toList();

        

    }
}
