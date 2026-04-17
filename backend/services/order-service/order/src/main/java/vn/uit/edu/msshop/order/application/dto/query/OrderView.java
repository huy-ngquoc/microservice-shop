package vn.uit.edu.msshop.order.application.dto.query;

import java.util.List;

import vn.uit.edu.msshop.order.domain.model.valueobject.CreateAt;
import vn.uit.edu.msshop.order.domain.model.valueobject.Currency;
import vn.uit.edu.msshop.order.domain.model.valueobject.Discount;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderDetail;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.OriginPrice;
import vn.uit.edu.msshop.order.domain.model.valueobject.PaymentMethod;
import vn.uit.edu.msshop.order.domain.model.valueobject.PaymentStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.ShippingFee;
import vn.uit.edu.msshop.order.domain.model.valueobject.ShippingInfo;
import vn.uit.edu.msshop.order.domain.model.valueobject.TotalPrice;
import vn.uit.edu.msshop.order.domain.model.valueobject.UpdateAt;
import vn.uit.edu.msshop.order.domain.model.valueobject.UserId;

public record OrderView(
    OrderId orderId,
    ShippingInfo shippingInfo,
    List<OrderDetail> details,
    OrderStatus status,
    UserId userId,
    OriginPrice originPrice,
    ShippingFee shippingFee,
    Discount discount,
    TotalPrice totalPrice,
    CreateAt createAt,
    UpdateAt updateAt,
    PaymentMethod paymentMethod,
    Currency currency,
    PaymentStatus paymentStatus
) {

}
