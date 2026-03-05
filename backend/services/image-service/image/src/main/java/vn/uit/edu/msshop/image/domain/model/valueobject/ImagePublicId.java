package vn.uit.edu.msshop.image.domain.model.valueobject;
public record ImagePublicId(String value) {
    public ImagePublicId {
         if(value==null||value.isBlank()) {
            throw new IllegalArgumentException("Invalid public id");
        }
    }
}
