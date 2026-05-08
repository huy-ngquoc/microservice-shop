package vn.uit.edu.msshop.image.domain.model.valueobject;
public record ImageUrl(String value) {
    public ImageUrl {
        if(value==null||value.isBlank()) {
            throw new IllegalArgumentException("Invalid url");
        }
    }
    
}
