package vn.edu.uit.msshop.product.shared.event.domain;

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
public class VariantCreated {
        private UUID variantId;
        private UUID productId;
        private String productName;
        private long price;
        private List<String> traits;
        private String imageKey;
}