package vn.edu.uit.msshop.product.shared.event.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor

@Builder
public class VariantCreated {
        private UUID variantId;
        private UUID productId;
        private String productName;
        private long price;
        private List<String> traits;
        @Nullable
        private String imageKey;
}