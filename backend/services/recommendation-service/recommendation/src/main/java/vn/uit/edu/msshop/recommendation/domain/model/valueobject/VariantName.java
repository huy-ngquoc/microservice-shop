package vn.uit.edu.msshop.recommendation.domain.model.valueobject;
public record VariantName(String value) {
    public VariantName {
        if(value==null||value.isBlank()) throw new IllegalArgumentException("Invalid name");
    }
}
