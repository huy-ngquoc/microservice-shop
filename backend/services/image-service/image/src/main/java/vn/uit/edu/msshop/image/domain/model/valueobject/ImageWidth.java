package vn.uit.edu.msshop.image.domain.model.valueobject;
public record ImageWidth(int value) {
    public ImageWidth {
        if(value<=0) {
            throw new IllegalArgumentException("Invalid width");
        }
    }

}
