package vn.edu.uit.msshop.product.domain.model.brand.valueobject;

public record BrandLogoKey(
        String value) {
    public BrandLogoKey {
        if (value == null) {
            throw new IllegalArgumentException("publicId null");
        }

        value = value.trim();

        if (value.isBlank()) {
            throw new IllegalArgumentException("publicId blank");
        }
    }
}
