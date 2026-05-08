package vn.uit.edu.msshop.cart.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection="cart-document")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDocument {
    private UUID userId;
    private List<CartItemDocument> items;
}
