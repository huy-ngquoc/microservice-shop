package vn.uit.edu.msshop.image.domain.model.valueobject;
public record ImageSize(int value) {
    public ImageSize {
        if(value<=0) {
            throw new IllegalArgumentException("Invalid size");
        }
    }
}
