package vn.uit.edu.payment.adapter.in.web.request;

import java.util.UUID;

import vn.uit.edu.payment.adapter.in.web.request.common.ChangeRequest;

public record UpdatePaymentRequest(
    UUID paymentId,
    ChangeRequest<String> currency,
    ChangeRequest<String> paymentStatus,
    ChangeRequest<String> paymentMethod
    
) {

}
