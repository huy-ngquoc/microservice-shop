package vn.uit.edu.msshop.order.application.service;

import org.springframework.stereotype.Service;

import vn.uit.edu.msshop.order.adapter.in.web.request.CreateOrderRequest;
import vn.uit.edu.msshop.order.application.port.out.CalculateShippingFeeUseCase;

@Service

public class CalculateShippingFeeService implements CalculateShippingFeeUseCase {

    @Override
    public long calculateShippingFee(CreateOrderRequest request) {
        return 0;
    }
    
}
