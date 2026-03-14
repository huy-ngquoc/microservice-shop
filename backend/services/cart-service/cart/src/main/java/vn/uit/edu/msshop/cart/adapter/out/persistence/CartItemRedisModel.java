package vn.uit.edu.msshop.cart.adapter.out.persistence;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRedisModel {
    private String variantId;
    private List<String> imageUrls;
    private String name;
    private BigDecimal price; 
    private String color;
    private String size;
    private int amount;

}
