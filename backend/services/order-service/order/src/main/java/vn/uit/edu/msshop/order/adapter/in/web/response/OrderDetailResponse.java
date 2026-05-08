package vn.uit.edu.msshop.order.adapter.in.web.response;

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
