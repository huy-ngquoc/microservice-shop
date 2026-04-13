package vn.uit.edu.msshop.order.domain.model.valueobject;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.Builder;


@Builder
public record OrderDetail(UUID variantId,UUID productId, String productName,  String  imageKey, int amount, long unitPrice, List<String> traits) {
    private static final Set<String> VALID_SIZE = Set.of("XXS","XS","S","M","L","XL","XXL");
    public OrderDetail {
        if(variantId==null) {
            throw new IllegalArgumentException("Invalid variant id");
        }
        if(amount<=0) {
            throw new IllegalArgumentException("Invalid amount");
        } 
        
        if(unitPrice<=0){
            throw new IllegalArgumentException("Invalid unit price");
        }
    }

}
