package vn.uit.edu.msshop.order.adapter.in.web.response;
public record OrderDetailResponse(
    String variantId,
    String variantName,
    String productName,
    String size,
    String color,
    String[] image,
    int amount,
    long unitPrice
) {

}
