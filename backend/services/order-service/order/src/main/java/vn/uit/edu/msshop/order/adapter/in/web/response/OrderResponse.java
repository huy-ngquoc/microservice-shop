package vn.uit.edu.msshop.order.adapter.in.web.response;

import java.time.Instant;
import java.util.List;

public record OrderResponse(
    String orderId,
    String fullName,
    String address,
    String email,
    String phone,
    List<OrderDetailResponse> detailResponses,
    long shippingFee,
    long discount,
    String status,
    Instant createAt
) {

}
