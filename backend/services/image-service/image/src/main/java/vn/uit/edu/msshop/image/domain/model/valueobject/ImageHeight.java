package vn.uit.edu.msshop.image.domain.model.valueobject;
public record ImageHeight(int value) {
    public ImageHeight {
        if(value<=0) {
            throw new IllegalArgumentException("Invalid height");
        }
    }

}
