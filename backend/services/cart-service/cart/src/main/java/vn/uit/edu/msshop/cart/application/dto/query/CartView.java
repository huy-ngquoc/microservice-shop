package vn.uit.edu.msshop.cart.application.dto.query;

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
public class CartView {
    private UUID userId;
    private List<CartDetailView> detailViews;
}
