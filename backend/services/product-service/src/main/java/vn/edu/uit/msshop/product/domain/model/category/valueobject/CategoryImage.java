package vn.edu.uit.msshop.product.domain.model.category.valueobject;

public record CategoryImage(
        CategoryImageUrl url,
        CategoryImageKey key,
        CategoryImageSize size) {
    public CategoryImage {
        if ((url == null) || (key == null) || (size == null)) {
            throw new IllegalArgumentException("Image fields must not be null");
        }
    }
}
