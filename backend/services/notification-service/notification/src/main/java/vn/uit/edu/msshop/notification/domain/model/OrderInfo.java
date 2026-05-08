package vn.uit.edu.msshop.notification.domain.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.uit.edu.msshop.notification.domain.model.valueobject.OrderCurrency;
import vn.uit.edu.msshop.notification.domain.model.valueobject.OrderDetail;
import vn.uit.edu.msshop.notification.domain.model.valueobject.OrderDiscount;
import vn.uit.edu.msshop.notification.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.notification.domain.model.valueobject.OrderTotalPrice;
import vn.uit.edu.msshop.notification.domain.model.valueobject.OriginOrderValue;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderInfo {
    private OrderId orderId;
    private OrderCurrency orderCurrency;
    private List<OrderDetail> orderDetails;
    private OrderDiscount orderDiscount;
    private OrderTotalPrice orderTotalPrice;
    private OriginOrderValue originOrderValue;
    
}
