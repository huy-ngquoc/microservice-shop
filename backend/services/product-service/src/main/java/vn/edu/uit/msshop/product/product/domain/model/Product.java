package vn.edu.uit.msshop.product.product.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductDeletionTime;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductImageKey;
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
    public Product replaceKey(ProductImageKey oldImageKey, ProductImageKey newImageKey) {
        List<ProductImageKey> newKeys = new ArrayList<>();
        for(ProductImageKey key: this.imageKeys.values()) {
            if(key.value().equals(oldImageKey.value())) {
                newKeys.add(newImageKey);
            }
            else newKeys.add(key);

        }
        return new Product(id, name, categoryId, brandId, configuration, new ProductImageKeys(newKeys), version, deletionTime);
    }
    public Product addKey(ProductImageKey newKey) {
        List<ProductImageKey> newKeys = this.imageKeys.values();
        newKeys.add(newKey);
        return new Product(id, name, categoryId, brandId, configuration, new ProductImageKeys(newKeys), version, deletionTime);
    }
    public Product removeKey(ProductImageKey deletedKey) {
        List<ProductImageKey> newKeys = new ArrayList<>();
        for(ProductImageKey key: this.imageKeys.values()) {
            if(!key.value().equals(deletedKey.value())) {
                newKeys.add(key);
            }
            

        }
        return new Product(id, name, categoryId, brandId, configuration, new ProductImageKeys(newKeys), version, deletionTime);
    }
    public Product reorderImages(List<Integer> newIndexes) {
        
        if (newIndexes == null || newIndexes.size() != this.imageKeys.values().size()) {
            throw new IllegalArgumentException("Danh sách vị trí mới không hợp lệ hoặc thiếu ảnh.");
        }
        Set<Integer> uniqueIndexes = Set.copyOf(newIndexes);
        if (uniqueIndexes.size() != this.imageKeys.values().size()) {
            throw new IllegalArgumentException("Các vị trí hoán đổi không được trùng lặp.");
        }
        List<ProductImageKey> reorderedList = new ArrayList<>();
        for (int index : newIndexes) {
            if (index < 0 || index >= this.imageKeys.values().size()) {
                throw new IllegalArgumentException("Vị trí " + index + " nằm ngoài phạm vi danh sách ảnh.");
            }
            reorderedList.add(this.imageKeys.values().get(index));
        }

        return new Product(id, name, categoryId, brandId, configuration, new ProductImageKeys(reorderedList), version, deletionTime);
    
        
    }
}
