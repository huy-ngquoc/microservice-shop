package vn.edu.uit.msshop.product.brand.domain.model.creation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandName;
import vn.edu.uit.msshop.shared.domain.Domains;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class NewBrand {
    @EqualsAndHashCode.Include
    private final BrandId id;

    private final BrandName name;

    public NewBrand(
            final BrandId id,
            final BrandName name) {
        this.id = Domains.requireNonNull(id, "Id must NOT be null");
        this.name = Domains.requireNonNull(name, "Name must NOT be null");
    }
}
