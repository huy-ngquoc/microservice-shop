package vn.edu.uit.msshop.product.category.domain.model.creation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryName;
import vn.edu.uit.msshop.product.shared.domain.Domains;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class NewCategory {
    @EqualsAndHashCode.Include
    private final CategoryId id;

    private final CategoryName name;

    public NewCategory(
            final CategoryId id,
            final CategoryName name) {
        this.id = Domains.requireNonNull(id, "ID must NOT be null");
        this.name = Domains.requireNonNull(name, "Name must NOT be null");
    }
}
