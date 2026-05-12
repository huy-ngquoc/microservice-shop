package vn.uit.edu.msshop.order.adapter.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class VariantNotFoundException extends RuntimeException {
    public VariantNotFoundException(UUID variantId) {
        super("Variant does not exist with id "+variantId.toString());
    }
    public VariantNotFoundException(String message) {
        super(message);
    }
}
