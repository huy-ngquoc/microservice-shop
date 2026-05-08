package vn.uit.edu.msshop.order.application.dto.command;

import java.util.List;

import org.jspecify.annotations.NonNull;

import vn.uit.edu.msshop.order.domain.model.valueobject.Currency;
import vn.uit.edu.msshop.order.domain.model.valueobject.Discount;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.PaymentMethod;
import vn.uit.edu.msshop.order.domain.model.valueobject.ShippingFee;
import vn.uit.edu.msshop.order.domain.model.valueobject.ShippingInfo;
import vn.uit.edu.msshop.order.domain.model.valueobject.UserId;

public record CreateOrderCommand(
    @NonNull
    OrderId id,
    ShippingInfo shippingInfo,
    List<OrderDetailCommand> details,
    UserId userId,
    ShippingFee shippingFee,
    Discount discount,
    Currency currency,
    PaymentMethod paymentMethod
) {

}
