package vn.uit.edu.msshop.order.application.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class VariantNotFoundException extends RuntimeException {
    public VariantNotFoundException(UUID variantId) {
        super("Order not found with id "+variantId.toString());
    }
}
