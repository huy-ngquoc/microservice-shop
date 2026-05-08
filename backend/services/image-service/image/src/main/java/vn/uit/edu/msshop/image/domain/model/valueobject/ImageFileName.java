package vn.uit.edu.msshop.image.domain.model.valueobject;
public record ImageFileName(String value) {
    public ImageFileName {
         if(value==null||value.isBlank()) {
            throw new IllegalArgumentException("Invalid file name");
        }
    }
}
