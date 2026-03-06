package vn.uit.edu.msshop.image.domain.model.valueobject;
public record TimeStamp(long value) {
    public TimeStamp {
        if(value<=0) {
            throw new IllegalArgumentException("Invalid timestamp");
        }
    }

}
