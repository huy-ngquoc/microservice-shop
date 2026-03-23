package vn.uit.edu.msshop.cart.adapter.out.persistence;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VariantToUserRedisModel {
    private String variantId;
    private List<String> userIds;
}
