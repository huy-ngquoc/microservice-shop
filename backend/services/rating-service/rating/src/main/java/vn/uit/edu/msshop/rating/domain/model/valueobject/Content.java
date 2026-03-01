package vn.uit.edu.msshop.rating.domain.model.valueobject;
public record Content(String value) {
    public Content {
        if(value==null) {
            throw new IllegalArgumentException("Content must not be null");
        }
    }
}
