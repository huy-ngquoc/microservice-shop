package vn.uit.edu.msshop.order.adapter.in.web.request;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NonNull;

public record CreateOrderRequest(
    @NonNull
    UUID id,
    String fullName,
    String address,
    String phone, 
    String email,
    List<OrderDetailRequest> detailRequests,
    UUID userId,
    long shippingFee,
    long totalPrice
) {

}
