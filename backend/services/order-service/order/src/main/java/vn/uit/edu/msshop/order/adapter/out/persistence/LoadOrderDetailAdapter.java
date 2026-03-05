package vn.uit.edu.msshop.order.adapter.out.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import vn.uit.edu.msshop.order.application.port.out.LoadOrderDetailPort;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderDetail;

public class LoadOrderDetailAdapter implements LoadOrderDetailPort{

    @Override
    public OrderDetail loadOrderDetail(UUID variantId, int quantity) {
        //TODO
        List<String> images = new ArrayList<>();
        return new OrderDetail(variantId, "Variant", "Product", "S", "Pink", images, quantity, 100000);
    }

}
