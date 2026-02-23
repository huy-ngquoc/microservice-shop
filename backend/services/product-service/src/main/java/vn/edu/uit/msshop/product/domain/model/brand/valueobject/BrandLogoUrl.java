package vn.edu.uit.msshop.product.domain.model.brand.valueobject;

public record BrandLogoUrl(
        String value) {
    public BrandLogoUrl {
        if (value == null) {
            throw new IllegalArgumentException("imageUrl null");
        }

        value = value.trim();

        if (!value.startsWith("http")) {
            throw new IllegalArgumentException("imageUrl invalid");
        }
    }
}
