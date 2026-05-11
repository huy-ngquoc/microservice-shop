package vn.uit.edu.msshop.cart.adapter.out.persistence;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartRedisModel {
    private String userId;
    private List<CartItemRedisModel> items;
}
