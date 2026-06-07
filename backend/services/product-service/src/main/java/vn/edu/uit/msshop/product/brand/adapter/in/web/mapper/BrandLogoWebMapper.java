package vn.edu.uit.msshop.product.brand.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.UpdateBrandLogoRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandLogoResponse;
import vn.edu.uit.msshop.product.brand.application.dto.command.logo.BrandLogoDeletionCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.logo.BrandLogoUpdateCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.shared.adapter.out.cloudinary.CloudinaryFolders;

@Component
@RequiredArgsConstructor
public class BrandLogoWebMapper {

    private final BrandLogoUrlResolver urlResolver;

    public BrandId toBrandId(
            final UUID id) {
        return new BrandId(id);
    }

    public BrandLogoUpdateCommand toUpdateCommand(
            final UUID brandId,
            final UpdateBrandLogoRequest request) {
        return new BrandLogoUpdateCommand(
                brandId,
                BrandLogoWebMapper.extractKeyFromTempPublicId(request.newLogoKey()),
                request.version());
    }

    public BrandLogoDeletionCommand toDeletionCommand(
            final UUID brandId,
            final long brandVersion) {
        return new BrandLogoDeletionCommand(
                brandId,
                brandVersion);
    }

    private static String extractKeyFromTempPublicId(
            final String publicId) {
        final var prefix = CloudinaryFolders.TEMP + "/";
        if (!publicId.startsWith(prefix)) {
            throw new IllegalArgumentException("Image key must be in temp folder");
        }

        return publicId.substring(prefix.length());
    }

    public BrandLogoResponse toLogoResponse(
            final BrandLogoView view) {
        return new BrandLogoResponse(view.id(),
                this.urlResolver.toLogoUrlString(view.logoKey()),
                view.version());
    }
}
