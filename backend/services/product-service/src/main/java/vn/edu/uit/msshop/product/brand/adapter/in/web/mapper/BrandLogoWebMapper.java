package vn.edu.uit.msshop.product.brand.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.UpdateBrandLogoRequest;
import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLogoLifecycleCommands;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;
import vn.edu.uit.msshop.shared.adapter.out.cloudinary.CloudinaryFolders;

@Component
@RequiredArgsConstructor
public class BrandLogoWebMapper {

    public BrandLogoLifecycleCommands.Update toUpdateLogoCommand(
            final UUID id,
            final UpdateBrandLogoRequest request) {
        final var brandId = new BrandId(id);
        final var logoKey = BrandLogoWebMapper.extractKeyFromTempPublicId(request.newLogoKey());
        final var version = new BrandVersion(request.version());

        return new BrandLogoLifecycleCommands.Update(brandId, logoKey, version);
    }

    public BrandLogoLifecycleCommands.Delete toDeleteLogoCommand(
            final UUID id,
            final long expectedVersion) {
        final var brandId = new BrandId(id);
        final var version = new BrandVersion(expectedVersion);

        return new BrandLogoLifecycleCommands.Delete(brandId, version);
    }

    private static BrandLogoKey extractKeyFromTempPublicId(
            final String publicId) {
        final var prefix = CloudinaryFolders.TEMP + "/";
        if (!publicId.startsWith(prefix)) {
            throw new IllegalArgumentException("Image key must be in temp folder");
        }

        return new BrandLogoKey(publicId.substring(prefix.length()));
    }
}
