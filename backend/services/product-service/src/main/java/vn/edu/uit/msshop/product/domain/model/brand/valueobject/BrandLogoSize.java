package vn.edu.uit.msshop.product.domain.model.brand.valueobject;

public record BrandLogoSize(
        int width,
        int height) {
    public BrandLogoSize {
        if ((width <= 0) || (height <= 0)) {
            throw new IllegalArgumentException("invalid image size");
        }
    }
}
