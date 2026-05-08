package vn.edu.uit.msshop.product.product.domain.model.creation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.shared.domain.Domains;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class NewProduct {
    @EqualsAndHashCode.Include
    private final ProductId id;

    private final ProductName name;

    private final ProductCategoryId categoryId;

    private final ProductBrandId brandId;

    private final ProductConfiguration configuration;

    public NewProduct(
            final ProductId id,
            final ProductName name,
            final ProductCategoryId categoryId,
            final ProductBrandId brandId,
            final ProductConfiguration configuration) {
        this.id = Domains.requireNonNull(id, "Product ID CANNOT be null");
        this.name = Domains.requireNonNull(name, "Product name CANNOT be null");
        this.categoryId = Domains.requireNonNull(categoryId, "Category ID CANNOT be null");
        this.brandId = Domains.requireNonNull(brandId, "Brand ID CANNOT be null");
        this.configuration = Domains.requireNonNull(configuration, "Configuration CANNOT be null");
    }

    public ProductOptions getOptions() {
        return this.configuration.options();
    }

    public ProductVariants getVariants() {
        return this.configuration.variants();
    }
}
