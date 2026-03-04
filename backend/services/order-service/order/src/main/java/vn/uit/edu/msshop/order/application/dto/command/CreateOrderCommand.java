package vn.uit.edu.msshop.order.application.dto.command;

import java.util.List;

import org.jspecify.annotations.NonNull;

import vn.uit.edu.msshop.order.domain.model.valueobject.Discount;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderDetail;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.ShippingFee;
import vn.uit.edu.msshop.order.domain.model.valueobject.ShippingInfo;
import vn.uit.edu.msshop.order.domain.model.valueobject.UserId;

public record CreateOrderCommand(
    @NonNull
    OrderId id,
    ShippingInfo shippingInfo,
    List<OrderDetail> details,
    UserId userId,
    ShippingFee shippingFee,
    Discount discount
) {

}
