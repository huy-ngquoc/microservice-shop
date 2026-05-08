package vn.uit.edu.payment.adapter.in.web.response;

import java.util.List;

public record OrderDetailResponse(
    String variantId,
    String productId,
    String productName,
    List<String> traits,
    int amount,
    long unitPrice
) {

}
