package vn.uit.edu.msshop.cart.adapter.in.web.response;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private UUID userId;
    private List<CartDetailResponse> detailResponses;
}
