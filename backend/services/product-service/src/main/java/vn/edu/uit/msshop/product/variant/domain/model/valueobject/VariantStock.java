package vn.edu.uit.msshop.product.variant.domain.model.valueobject;

public record VariantStock(
        int value) {
    public VariantStock {
        if (value < 0)
            throw new IllegalArgumentException("Invalid stock");
    }
}
