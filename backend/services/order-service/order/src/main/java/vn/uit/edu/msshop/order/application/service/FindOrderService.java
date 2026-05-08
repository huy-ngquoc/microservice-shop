package vn.uit.edu.msshop.order.application.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.application.dto.query.OrderView;
import vn.uit.edu.msshop.order.application.exception.OrderNotFoundException;
import vn.uit.edu.msshop.order.application.mapper.OrderViewMapper;
import vn.uit.edu.msshop.order.application.port.in.FindOrderUseCase;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderPort;
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
    public OrderView findOrderById(OrderId id) {
        Order order = loadPort.loadById(id).orElseThrow(()->new OrderNotFoundException(id));
        return mapper.toView(order);
    }

    @Override
    public Page<OrderView> filterOrder(Optional<UUID> variantId, Optional<OrderStatus> status, Optional<Integer> minPrice, Optional<Integer> maxPrice, Optional<UserId> userId, Optional<Instant> createFrom, Optional<Instant> createTo, int pageNumber, int pageSize) {
        Page<Order> pageOrder = loadPort.filterOrder(variantId, status, minPrice, maxPrice, userId, createFrom, createTo, pageNumber, pageSize);
        return pageOrder.map(this.mapper::toView);
    }

    @Override
    public List<OrderView> findAll() {
        return loadPort.loadAll().stream().map(mapper::toView).toList();
    }

}
