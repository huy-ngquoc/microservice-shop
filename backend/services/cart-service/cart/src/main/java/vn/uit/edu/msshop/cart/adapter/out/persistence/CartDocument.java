package vn.uit.edu.msshop.cart.adapter.out.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(
        collection = "cart-document")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDocument implements Serializable {
    @Id
    private UUID userId;
    private List<CartItemDocument> items;
}
