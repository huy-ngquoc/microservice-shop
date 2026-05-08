package vn.uit.edu.msshop.order.adapter.in.web.request;

import java.util.UUID;

import vn.uit.edu.msshop.order.adapter.in.web.request.common.ChangeRequest;







public record UpdateOrderRequest(
    UUID id,
    ChangeRequest<String> fullName,
    ChangeRequest<String> email,
    ChangeRequest<String> address,
    ChangeRequest<String> phone,
    ChangeRequest<String> orderStatus
) {

}
