package vn.uit.edu.msshop.order.application.port.in;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import vn.uit.edu.msshop.order.application.dto.query.OrderView;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.UserId;

public interface FindOrderUseCase {
    public OrderView findOrderById(OrderId id);
    public Page<OrderView> filterOrder(Optional<UUID> variantId, Optional<OrderStatus> status, Optional<Integer> minPrice, Optional<Integer> maxPrice, Optional<UserId> userId,Optional<Instant> createFrom, Optional<Instant> createTo);
}
