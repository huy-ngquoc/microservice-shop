package vn.edu.uit.msshop.product.product.domain.model;

import java.util.List;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductImages(
        List<ProductImage> values) {
    public static final int MAX_AMOUNT = 10;

    public ProductImages {
        if (values == null) {
            throw new DomainException("Product images list CANNOT be null");
        }

        if (values.isEmpty()) {
            throw new DomainException("Product MUST have at least 1 image");
        }

        if (values.size() > MAX_AMOUNT) {
            throw new DomainException("Product CANNOT exceed " + MAX_AMOUNT + " images");
        }

        values = List.copyOf(values);
    }

    public ProductImage getCoverImage() {
        // Safe as list never empty (verified in constructor).
        return values.getFirst();
    }
}
