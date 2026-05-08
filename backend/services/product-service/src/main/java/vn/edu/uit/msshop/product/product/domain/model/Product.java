package vn.edu.uit.msshop.product.product.domain.model;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductDeletionTime;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductImageKeys;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductPriceRange;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;
import vn.edu.uit.msshop.shared.domain.Domains;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
// TODO: add status
public final class Product {
    @EqualsAndHashCode.Include
    private final ProductId id;

    private final ProductName name;

    private final ProductCategoryId categoryId;

    private final ProductBrandId brandId;

    private final ProductConfiguration configuration;

    private final ProductImageKeys imageKeys;

    // ===== Metadata =====

    private final ProductVersion version;

    @Nullable
    private final ProductDeletionTime deletionTime;

    public Product(
            final ProductId id,
            final ProductName name,
            final ProductCategoryId categoryId,
            final ProductBrandId brandId,
            final ProductConfiguration configuration,
            final ProductImageKeys imageKeys,
            final ProductVersion version,
            @Nullable
            final ProductDeletionTime deletionTime) {
        this.id = Domains.requireNonNull(id, "Product ID CANNOT be null");
        this.name = Domains.requireNonNull(name, "Product name CANNOT be null");
        this.categoryId = Domains.requireNonNull(categoryId, "Category ID CANNOT be null");
        this.brandId = Domains.requireNonNull(brandId, "Brand ID CANNOT be null");
        this.configuration = Domains.requireNonNull(configuration, "Configuration CANNOT be null");
        this.imageKeys = Domains.requireNonNull(imageKeys, "Product image keys CANNOT be null");
        this.version = Domains.requireNonNull(version, "Product version CANNOT be null");
        this.deletionTime = deletionTime;
    }

    public ProductOptions getOptions() {
        return this.configuration.options();
    }

    public ProductVariants getVariants() {
        return this.configuration.variants();
    }

    public ProductPriceRange getPriceRange() {
        return this.getVariants().getPriceRange();
    }
}
