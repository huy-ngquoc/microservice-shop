package vn.edu.uit.msshop.product.variant.adapter.in.web.request;

import java.util.List;

import vn.edu.uit.msshop.product.shared.adapter.in.web.request.ChangeRequest;

public record UpdateVariantInfoRequest(
        ChangeRequest<Integer> price,
        ChangeRequest<List<String>> traits) {
}
