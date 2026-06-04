package vn.edu.uit.msshop.product.product.application.dto.command;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductImageKeys;



public record ReorderImageCommand(ProductId productId, List<Integer> newIndexes, long version){
    
}
