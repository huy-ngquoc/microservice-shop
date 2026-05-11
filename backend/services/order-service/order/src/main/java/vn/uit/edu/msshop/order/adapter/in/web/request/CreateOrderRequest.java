package vn.uit.edu.msshop.order.adapter.in.web.request;

import java.util.List;

public record CreateOrderRequest(
   
    String fullName,
    String address,
    String phone, 
    String email,
    List<OrderDetailRequest> detailRequests,
    
    long discount,
    String currency,
    String paymentMethod
) {

}
