package vn.edu.uit.msshop.product.product.domain.model.valueobject;

public record IncreaseAmount(int value) {
    public IncreaseAmount {
        if(value<=0) throw new IllegalArgumentException("Invalid increase amount");
    }
}
