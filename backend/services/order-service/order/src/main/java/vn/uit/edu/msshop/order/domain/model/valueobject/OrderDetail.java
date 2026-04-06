package vn.uit.edu.msshop.order.domain.model.valueobject;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
public record OrderDetail(UUID variantId, String variantName, String productName, String size, String color, List<String> images, int amount, long unitPrice) {
    private static final Set<String> VALID_SIZE = Set.of("XXS","XS","S","M","L","XL","XXL");
    public OrderDetail {
        if(variantId==null) {
            throw new IllegalArgumentException("Invalid variant id");
        }
        if(amount<=0) {
            throw new IllegalArgumentException("Invalid amount");
        } 
        if(!VALID_SIZE.contains(size)) {
            throw new IllegalArgumentException("Invalid size");
        }
        if(color==null||color.isBlank()) {
            throw new IllegalArgumentException("Invalid color");
        }
        if(unitPrice<=0){
            throw new IllegalArgumentException("Invalid unit price");
        }
    }

}
