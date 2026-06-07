package vn.edu.uit.msshop.product.brand.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.UpdateBrandLogoRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandLogoResponse;
import vn.edu.uit.msshop.product.brand.application.dto.command.logo.BrandLogoDeletionByIdCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.logo.BrandLogoUpdateByIdCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.shared.adapter.out.cloudinary.CloudinaryPublicIds;

@Component
@RequiredArgsConstructor
public class BrandLogoWebMapper {

    private final BrandLogoUrlResolver urlResolver;

    public BrandId toBrandId(
            final UUID id) {
        return new BrandId(id);
    }

    public BrandLogoUpdateByIdCommand toUpdateCommand(
            final UUID brandId,
            final UpdateBrandLogoRequest request) {
        final var logoKey = CloudinaryPublicIds.extractKeyFromTemp(request.logoId());
        return new BrandLogoUpdateByIdCommand(
                brandId,
                logoKey,
                request.version());
    }

    public BrandLogoDeletionByIdCommand toDeletionCommand(
            final UUID brandId,
            final long brandVersion) {
        return new BrandLogoDeletionByIdCommand(
                brandId,
                brandVersion);
    }

    public BrandLogoResponse toLogoResponse(
            final BrandLogoView view) {
        return new BrandLogoResponse(view.id(),
                this.urlResolver.toLogoUrlString(view.logoKey()),
                view.version());
    }
}
