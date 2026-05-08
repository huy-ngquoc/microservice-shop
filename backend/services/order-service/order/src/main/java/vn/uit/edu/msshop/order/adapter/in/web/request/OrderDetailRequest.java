package vn.uit.edu.msshop.order.adapter.in.web.request;

import java.util.UUID;

public record OrderDetailRequest(
    UUID variantId,
    int quantity) {

}
