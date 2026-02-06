package vn.edu.uit.msshop.profile.domain.model.valueobject;

public record ImageSize(
        int width,
        int height) {
    public ImageSize {
        if ((width <= 0) || (height <= 0)) {
            throw new IllegalArgumentException("invalid image size");
        }
    }
}
