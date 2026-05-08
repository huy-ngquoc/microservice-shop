package vn.uit.edu.msshop.order.application.dto.query;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VariantSoldCountView {
    private UUID variantId;
    private int soldCount;
}
