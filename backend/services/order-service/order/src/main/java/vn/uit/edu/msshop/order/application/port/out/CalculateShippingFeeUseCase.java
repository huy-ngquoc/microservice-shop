package vn.uit.edu.msshop.order.application.port.out;

import vn.uit.edu.msshop.order.adapter.in.web.request.CreateOrderRequest;

public interface CalculateShippingFeeUseCase {
    public long calculateShippingFee(CreateOrderRequest request);
}
