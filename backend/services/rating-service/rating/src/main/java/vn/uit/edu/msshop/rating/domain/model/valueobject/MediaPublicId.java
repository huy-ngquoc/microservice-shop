package vn.uit.edu.msshop.rating.domain.model.valueobject;
public record MediaPublicId(String value) {
    public MediaPublicId {
        if(value==null) {
            throw new IllegalArgumentException("Public id must not be null");
        }
        if(value.isBlank()) {
            throw new IllegalArgumentException("Public id must not be blank");
        }
    }
}
