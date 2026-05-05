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
import vn.edu.uit.msshop.product.brand.application.dto.command.CreateBrandCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.DeleteBrandLogoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.HardDeleteBrandCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.RestoreBrandCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.SoftDeleteBrandCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.UpdateBrandInfoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.UpdateBrandLogoCommand;
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

    public CreateBrandCommand toCreateCommand(
            final CreateBrandRequest request) {
        final var name = new BrandName(request.name());

        return new CreateBrandCommand(
                name);
    }

    public RestoreBrandCommand toRestoreCommand(
            final UUID id,
            final long expectedVersion) {
        final var brandId = new BrandId(id);
        final var version = new BrandVersion(expectedVersion);

        return new RestoreBrandCommand(
                brandId,
                version);
    }

    public UpdateBrandInfoCommand toUpdateInfoCommand(
            final UUID id,
            final UpdateBrandInfoRequest request) {
        final var brandId = new BrandId(id);
        final var version = new BrandVersion(request.version());

        final var name = ChangeRequest.toChange(request.name(), BrandName::new);

        return new UpdateBrandInfoCommand(
                brandId,
                name,
                version);
    }

    public UpdateBrandLogoCommand toUpdateLogoCommand(
            final UUID id,
            final UpdateBrandLogoRequest request) {
        final var brandId = new BrandId(id);
        final var logoKey = BrandWebMapper.extractKeyFromTempPublicId(request.newLogoKey());
        final var version = new BrandVersion(request.version());

        return new UpdateBrandLogoCommand(
                brandId,
                logoKey,
                version);
    }

    public DeleteBrandLogoCommand toDeleteLogoCommand(
            final UUID id,
            final long expectedVersion) {
        final var brandId = new BrandId(id);
        final var version = new BrandVersion(expectedVersion);

        return new DeleteBrandLogoCommand(
                brandId,
                version);
    }

    public SoftDeleteBrandCommand toSoftDeleteCommand(
            final UUID id,
            final long expectedVersion) {
        final var brandId = new BrandId(id);
        final var version = new BrandVersion(expectedVersion);

        return new SoftDeleteBrandCommand(
                brandId,
                version);
    }

    public HardDeleteBrandCommand toHardDeleteCommand(
            final UUID id,
            final long expectedVersion) {
        final var brandId = new BrandId(id);
        final var version = new BrandVersion(expectedVersion);

        return new HardDeleteBrandCommand(
                brandId,
                version);
    }

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
        return new BrandLogoResponse(
                view.id(),
                this.toLogoUrlString(view.logoKey()),
                view.version());
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
            @Nullable final String keyString) {
        return this.urlResolver.resolve(
                keyString,
                BrandLogoStorageAdapter.BRAND_FOLDER);
    }
}
