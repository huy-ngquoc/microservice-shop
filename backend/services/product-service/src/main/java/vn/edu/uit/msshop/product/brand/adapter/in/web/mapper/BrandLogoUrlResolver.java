package vn.edu.uit.msshop.product.brand.adapter.in.web.mapper;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.out.logo.BrandLogoStorageAdapter;
import vn.edu.uit.msshop.shared.adapter.out.cloudinary.CloudinaryImageUrlResolver;

@Component
@RequiredArgsConstructor
class BrandLogoUrlResolver {

    private final CloudinaryImageUrlResolver urlResolver;

    @Nullable
    public String toLogoUrlString(
            @Nullable
            final String keyString) {
        return this.urlResolver.resolve(keyString, BrandLogoStorageAdapter.BRAND_FOLDER);
    }
}
