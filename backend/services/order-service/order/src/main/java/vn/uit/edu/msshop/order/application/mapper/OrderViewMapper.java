package vn.uit.edu.msshop.order.application.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.order.application.dto.query.OrderView;
import vn.uit.edu.msshop.order.domain.model.Order;
/* OrderStatus status,
    UserId userId,
    OriginPrice originPrice,
    ShippingFee shippingFee,
    Discount discount,
    TotalPrice totalPrice,
    CreateAt createAt,
    UpdateAt updateAt */
@Component
public class OrderViewMapper {
    public OrderView toView(Order order) {
        return new OrderView(order.getId(),order.getShippingInfo(),order.getDetails(),order.getStatus(),order.getUserId(),order.getOriginPrice()
    ,order.getShippingFee(),order.getDiscount(),order.getTotalPrice(),order.getCreateAt(),order.getUpdateAt(),order.getPaymentMethod(), order.getCurrency(), order.getPaymentStatus());
    }
}
