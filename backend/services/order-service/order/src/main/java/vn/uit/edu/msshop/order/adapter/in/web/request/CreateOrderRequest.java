package vn.uit.edu.msshop.order.adapter.in.web.request;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
   
    String fullName,
    String address,
    String phone, 
    String email,
    List<OrderDetailRequest> detailRequests,
    UUID userId,
    long shippingFee,
    
    long discount,
    String currency,
    String paymentMethod
) {

}
