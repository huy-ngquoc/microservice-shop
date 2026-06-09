package vn.edu.uit.msshop.product.category.adapter.in.web.mapper;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.adapter.out.image.CategoryImageStorageAdapter;
import vn.edu.uit.msshop.shared.adapter.out.cloudinary.CloudinaryImageUrlResolver;

@Component
@RequiredArgsConstructor
class CategoryLogoUrlResolver {

    private final CloudinaryImageUrlResolver urlResolver;

    @Nullable
    public String toLogoUrlString(
            @Nullable
            final String keyString) {
        return this.urlResolver.resolve(keyString, CategoryImageStorageAdapter.CATEGORY_FOLDER);
    }

}
