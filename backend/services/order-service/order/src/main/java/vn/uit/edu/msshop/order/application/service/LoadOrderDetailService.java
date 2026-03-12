package vn.uit.edu.msshop.order.application.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderDetailPort;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderDetail;

@Service
@RequiredArgsConstructor
public class LoadOrderDetailService implements LoadOrderDetailPort  {

    @Override
    public OrderDetail loadOrderDetail(UUID variantId, int quantity) {
        return new OrderDetail(variantId, "AO", "AO", "XXL", "NHO", new ArrayList<>(), quantity,100000);
    }
    
}
