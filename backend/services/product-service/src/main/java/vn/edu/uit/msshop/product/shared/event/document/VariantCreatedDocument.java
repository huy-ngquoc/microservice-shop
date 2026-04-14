package vn.edu.uit.msshop.product.shared.event.document;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariantCreatedDocument {
    private UUID variantId;
    private long price;
    private List<String> traits;
    private String imageKey;
}
