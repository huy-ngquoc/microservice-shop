package vn.uit.edu.msshop.notification.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDocument {
    private UUID orderId;
    private String orderCurrency;
    private List<OrderDetailDocument> orderDetailDocuments;
    private int orderDiscount;
    private int orderTotalPrice;
    private int orderOriginValue;
}
