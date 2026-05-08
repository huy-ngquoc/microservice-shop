package vn.uit.edu.msshop.image.domain.model.valueobject;
public record ImageSize(long value) {
    public ImageSize {
        if(value<=0) {
            throw new IllegalArgumentException("Invalid size");
        }
    }
}
