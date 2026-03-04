package vn.uit.edu.msshop.order.application.port.out;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.UserId;

public interface LoadOrderPort {
    public Optional<Order> loadById(OrderId orderId);
    public Page<Order> filterOrder(Optional<UUID> variantId, Optional<OrderStatus> status, Optional<Integer> minPrice, Optional<Integer> maxPrice, Optional<UserId> userId,Optional<Instant> createFrom, Optional<Instant> createTo);
}
