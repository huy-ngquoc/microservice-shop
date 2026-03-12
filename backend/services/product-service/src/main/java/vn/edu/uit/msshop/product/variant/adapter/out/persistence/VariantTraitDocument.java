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
public class VariantTraitDocument {
    private String name;
    private String value;
}
