package vn.uit.edu.msshop.cart.application.dto.query;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartView implements Serializable {
    private UUID userId;
    private List<CartDetailView> detailViews;
}
