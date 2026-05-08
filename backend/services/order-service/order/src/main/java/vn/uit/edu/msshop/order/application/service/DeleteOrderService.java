package vn.uit.edu.msshop.order.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.application.port.in.DeleteOrderUseCase;
import vn.uit.edu.msshop.order.application.port.out.DeleteOrderPort;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;

@Service
@RequiredArgsConstructor
public class DeleteOrderService implements DeleteOrderUseCase {
    private final DeleteOrderPort deletePort;

    @Override
    public void deleteById(OrderId orderId) {
         deletePort.deleteById(orderId);
    }

    @Override
    public void deleteAll() {
        deletePort.deleteAll();
    }
}
