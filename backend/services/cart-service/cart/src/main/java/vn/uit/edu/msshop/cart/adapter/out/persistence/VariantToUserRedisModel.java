package vn.uit.edu.msshop.cart.adapter.out.persistence;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="variant-to-user")
public class VariantToUserRedisModel {
    @Id
    private String variantId;
    private List<String> userIds;
}
