package vn.edu.uit.msshop.product.domain.model.category.valueobject;

public record CategoryImageSize(
        int width,
        int height) {
    public CategoryImageSize {
        if ((width <= 0) || (height <= 0)) {
            throw new IllegalArgumentException("invalid image size");
        }
    }
}
