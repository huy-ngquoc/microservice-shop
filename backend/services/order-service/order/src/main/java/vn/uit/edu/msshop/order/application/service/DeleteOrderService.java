package vn.uit.edu.msshop.order.application.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.application.port.in.DeleteOrderUseCase;
import vn.uit.edu.msshop.order.application.port.out.DeleteOrderPort;
import vn.uit.edu.msshop.order.bootstrap.config.cache.CacheNames;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;

@Service
@RequiredArgsConstructor
public class DeleteOrderService implements DeleteOrderUseCase {
    private final DeleteOrderPort deletePort;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.ORDERS,
            key = "#orderId.value()")
    public void deleteById(
            OrderId orderId) {
        deletePort.deleteById(orderId);
    }

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.ORDERS,
            allEntries = true)
    public void deleteAll() {
        deletePort.deleteAll();
    }
}
