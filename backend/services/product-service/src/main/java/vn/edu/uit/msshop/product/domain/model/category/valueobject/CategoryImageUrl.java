package vn.edu.uit.msshop.product.domain.model.category.valueobject;

public record CategoryImageUrl(
        String value) {
    public CategoryImageUrl {
        if (value == null) {
            throw new IllegalArgumentException("imageUrl null");
        }

        value = value.trim();

        if (!value.startsWith("http")) {
            throw new IllegalArgumentException("imageUrl invalid");
        }
    }
}
