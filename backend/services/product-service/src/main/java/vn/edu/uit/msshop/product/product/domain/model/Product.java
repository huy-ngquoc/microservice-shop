package vn.edu.uit.msshop.product.product.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductImageKeys;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductPriceRange;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;
import vn.edu.uit.msshop.product.shared.domain.Domains;

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

    private final ProductPriceRange priceRange;

    private final ProductSoldCount soldCount;

    private final ProductRating rating;

    private final ProductConfiguration configuration;

    private final ProductImageKeys imageKeys;

    // ===== Metadata =====

    private final ProductVersion version;

    // TODO: should we omit "priceRange"?
    // as it can be got from configuration.getVariant.getPriceRange()
    public Product(
            ProductId id,
            ProductName name,
            ProductCategoryId categoryId,
            ProductBrandId brandId,
            ProductPriceRange priceRange,
            ProductSoldCount soldCount,
            ProductRating rating,
            ProductConfiguration configuration,
            ProductImageKeys imageKeys,
            ProductVersion version) {
        this.id = Domains.requireNonNull(id, "Product ID CANNOT be null");
        this.name = Domains.requireNonNull(name, "Product name CANNOT be null");
        this.categoryId = Domains.requireNonNull(categoryId, "Category ID CANNOT be null");
        this.brandId = Domains.requireNonNull(brandId, "Brand ID CANNOT be null");
        this.priceRange = Domains.requireNonNull(priceRange, "Price range CANNOT be null");
        this.soldCount = Domains.requireNonNull(soldCount, "Sold count CANNOT be null");
        this.rating = Domains.requireNonNull(rating, "Rating CANNOT be null");
        this.configuration = Domains.requireNonNull(configuration, "Configuration CANNOT be null");
        this.imageKeys = Domains.requireNonNull(imageKeys, "Product image keys CANNOT be null");
        this.version = Domains.requireNonNull(version, "Product version CANNOT be null");
    }

    public ProductOptions getOptions() {
        return this.configuration.options();
    }

    public ProductVariants getVariants() {
        return this.configuration.variants();
    }
}
