package vn.edu.uit.msshop.product.brand.adapter.in.web.mapper;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.CreateBrandRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.UpdateBrandInfoRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.UpdateBrandLogoRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandLogoResponse;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandResponse;
import vn.edu.uit.msshop.product.brand.application.dto.command.CreateBrandCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.DeleteBrandLogoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.UpdateBrandInfoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.UpdateBrandLogoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.query.BrandLogoView;
import vn.edu.uit.msshop.product.brand.application.dto.query.BrandView;
import vn.edu.uit.msshop.product.brand.domain.model.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoKey;
import vn.edu.uit.msshop.product.brand.domain.model.BrandName;
import vn.edu.uit.msshop.product.brand.domain.model.BrandVersion;
import vn.edu.uit.msshop.product.shared.adapter.in.web.request.ChangeRequest;

@Component
@RequiredArgsConstructor
public class BrandWebMapper {
    private final Cloudinary cloudinary;

    public CreateBrandCommand toCreateCommand(
            final CreateBrandRequest request) {
        final var name = new BrandName(request.name());

        return new CreateBrandCommand(
                name);
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
        final var logoKey = this.extractKeyFromTempPublicId(request.newLogoKey());
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

    private BrandLogoKey extractKeyFromTempPublicId(
            final String publicId) {
        if (!publicId.startsWith("temp/")) {
            throw new IllegalArgumentException("Image key must be in temp folder");
        }

        return new BrandLogoKey(publicId.substring("temp/".length()));
    }

    private @Nullable String toLogoUrlString(
            @Nullable
            final String keyString) {
        if (keyString == null) {
            return null;
        }

        return this.cloudinary.url()
                .generate("brands/" + keyString);
    }
}
