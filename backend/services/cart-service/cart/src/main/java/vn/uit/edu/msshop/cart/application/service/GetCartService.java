package vn.uit.edu.msshop.cart.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.application.dto.query.CartView;
import vn.uit.edu.msshop.cart.application.mapper.CartViewMapper;
import vn.uit.edu.msshop.cart.application.port.in.GetCartUseCase;
import vn.uit.edu.msshop.cart.application.port.out.LoadCartPort;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;

@Service
@RequiredArgsConstructor
public class GetCartService implements GetCartUseCase {
    private final LoadCartPort loadPort;
    private final CartViewMapper mapper;

    @Override
    public CartView getByUserId(UserId userId) {
       return mapper.toView(loadPort.loadByUserId(userId));
    }

}
