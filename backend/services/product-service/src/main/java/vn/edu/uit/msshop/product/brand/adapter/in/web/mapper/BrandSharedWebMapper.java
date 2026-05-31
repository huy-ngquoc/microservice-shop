package vn.edu.uit.msshop.product.brand.adapter.in.web.mapper;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandLogoResponse;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandResponse;
import vn.edu.uit.msshop.product.brand.adapter.out.logo.BrandLogoStorageAdapter;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.shared.adapter.out.cloudinary.CloudinaryImageUrlResolver;

@Component
@RequiredArgsConstructor
public class BrandSharedWebMapper {

    private final CloudinaryImageUrlResolver urlResolver;

    public BrandId toBrandId(
            final UUID id) {
        return new BrandId(id);
    }

    public BrandResponse toResponse(
            final BrandView view) {
        return new BrandResponse(
                view.id(),
                view.name(),
                this.toLogoUrlString(view.logoKey()),
                view.version());
    }

    public BrandLogoResponse toLogoResponse(
            final BrandLogoView view) {
        return new BrandLogoResponse(view.id(),
                this.toLogoUrlString(view.logoKey()),
                view.version());
    }

    private @Nullable String toLogoUrlString(
            @Nullable
            final String keyString) {
        return this.urlResolver.resolve(keyString, BrandLogoStorageAdapter.BRAND_FOLDER);
    }
}
