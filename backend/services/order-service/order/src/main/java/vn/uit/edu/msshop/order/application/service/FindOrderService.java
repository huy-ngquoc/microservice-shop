package vn.uit.edu.msshop.order.application.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.application.dto.query.OrderView;
import vn.uit.edu.msshop.order.application.exception.OrderNotFoundException;
import vn.uit.edu.msshop.order.application.mapper.OrderViewMapper;
import vn.uit.edu.msshop.order.application.port.in.FindOrderUseCase;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderPort;
import vn.uit.edu.msshop.order.bootstrap.config.cache.CacheNames;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.UserId;

@Service
@RequiredArgsConstructor
public class FindOrderService implements FindOrderUseCase {
    private final LoadOrderPort loadPort;
    private final OrderViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.ORDERS,
            key = "#id.value()")
    public OrderView findOrderById(
            final OrderId id) {
        final Order order = loadPort.loadById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return mapper.toView(order);
    }

    @Override
    @Transactional(
            readOnly = true)
    public Page<OrderView> filterOrder(
            final Optional<UUID> variantId,
            final Optional<OrderStatus> status,
            final Optional<Integer> minPrice,
            final Optional<Integer> maxPrice,
            final Optional<UserId> userId,
            final Optional<Instant> createFrom,
            final Optional<Instant> createTo,
            final int pageNumber,
            final int pageSize) {
        final Page<Order> pageOrder = loadPort.filterOrder(
                variantId,
                status,
                minPrice,
                maxPrice,
                userId,
                createFrom,
                createTo,
                pageNumber,
                pageSize);
        return pageOrder.map(this.mapper::toView);
    }

    @Override
    @Transactional(
            readOnly = true)
    public List<OrderView> findAll() {
        return loadPort.loadAll().stream()
                .map(mapper::toView)
                .toList();
    }

}
