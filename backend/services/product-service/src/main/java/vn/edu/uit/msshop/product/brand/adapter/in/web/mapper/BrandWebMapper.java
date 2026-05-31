package vn.edu.uit.msshop.product.brand.adapter.in.web.mapper;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.CreateBrandRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.UpdateBrandInfoRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.UpdateBrandLogoRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandLogoResponse;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandResponse;
import vn.edu.uit.msshop.product.brand.adapter.out.logo.BrandLogoStorageAdapter;
import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLifecycleCommands;
import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLogoLifecycleCommands;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandName;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;
import vn.edu.uit.msshop.shared.adapter.in.web.request.ChangeRequest;
import vn.edu.uit.msshop.shared.adapter.out.cloudinary.CloudinaryFolders;
import vn.edu.uit.msshop.shared.adapter.out.cloudinary.CloudinaryImageUrlResolver;

@Component
@RequiredArgsConstructor
public class BrandWebMapper {
    private final CloudinaryImageUrlResolver urlResolver;

    public BrandLifecycleCommands.Create toCreateCommand(
            final CreateBrandRequest request) {
        final var name = new BrandName(request.name());

        return new BrandLifecycleCommands.Create(name);
    }

    public BrandLifecycleCommands.Restore toRestoreCommand(
            final UUID id,
            final long expectedVersion) {
        final var brandId = new BrandId(id);
        final var version = new BrandVersion(expectedVersion);

        return new BrandLifecycleCommands.Restore(brandId, version);
    }

    public BrandLifecycleCommands.UpdateInfo toUpdateInfoCommand(
            final UUID id,
            final UpdateBrandInfoRequest request) {
        final var brandId = new BrandId(id);
        final var version = new BrandVersion(request.version());

        final var name = ChangeRequest.toChange(request.name(), BrandName::new);

        return new BrandLifecycleCommands.UpdateInfo(brandId, name, version);
    }

    public BrandLogoLifecycleCommands.Update toUpdateLogoCommand(
            final UUID id,
            final UpdateBrandLogoRequest request) {
        final var brandId = new BrandId(id);
        final var logoKey = BrandWebMapper.extractKeyFromTempPublicId(request.newLogoKey());
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

    public BrandLifecycleCommands.SoftDelete toSoftDeleteCommand(
            final UUID id,
            final long expectedVersion) {
        final var brandId = new BrandId(id);
        final var version = new BrandVersion(expectedVersion);

        return new BrandLifecycleCommands.SoftDelete(brandId, version);
    }

    public BrandLifecycleCommands.HardDelete toHardDeleteCommand(
            final UUID id,
            final long expectedVersion) {
        final var brandId = new BrandId(id);
        final var version = new BrandVersion(expectedVersion);

        return new BrandLifecycleCommands.HardDelete(brandId, version);
    }

    public BrandId toBrandId(
            final UUID id) {
        return new BrandId(id);
    }

    public BrandResponse toResponse(
            final BrandView view) {
        return new BrandResponse(view.id(), view.name(), this.toLogoUrlString(view.logoKey()),
                view.version());
    }

    public BrandLogoResponse toLogoResponse(
            final BrandLogoView view) {
        return new BrandLogoResponse(view.id(), this.toLogoUrlString(view.logoKey()), view.version());
    }

    private static BrandLogoKey extractKeyFromTempPublicId(
            final String publicId) {
        final var prefix = CloudinaryFolders.TEMP + "/";
        if (!publicId.startsWith(prefix)) {
            throw new IllegalArgumentException("Image key must be in temp folder");
        }

        return new BrandLogoKey(publicId.substring(prefix.length()));
    }

    private @Nullable String toLogoUrlString(
            @Nullable
            final String keyString) {
        return this.urlResolver.resolve(keyString, BrandLogoStorageAdapter.BRAND_FOLDER);
    }
}
