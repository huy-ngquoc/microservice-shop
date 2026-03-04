package vn.uit.edu.msshop.order.application.dto.command;

import vn.uit.edu.msshop.order.application.common.Change;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.ShippingInfo;

public record UpdateOrderCommand(
    OrderId id,
    Change<ShippingInfo> shippingInfo,
    Change<OrderStatus> status
) {

}
