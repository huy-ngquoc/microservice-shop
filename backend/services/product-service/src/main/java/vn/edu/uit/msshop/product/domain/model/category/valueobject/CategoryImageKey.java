package vn.edu.uit.msshop.product.domain.model.category.valueobject;

public record CategoryImageKey(
        String value) {
    public CategoryImageKey {
        if (value == null) {
            throw new IllegalArgumentException("publicId null");
        }

        value = value.trim();

        if (value.isBlank()) {
            throw new IllegalArgumentException("publicId blank");
        }
    }
}
