package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("NullAway.Init")
@NoArgsConstructor(
        access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class VariantImageDocument {
    private String imageUrl;
    private String imageKey;
    private int imageWidth;
    private int imageHeight;
}
