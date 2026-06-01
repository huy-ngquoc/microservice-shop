package vn.edu.uit.msshop.product.brand.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.CreateBrandRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.UpdateBrandInfoRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandResponse;
import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLifecycleCommands;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandName;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;
import vn.edu.uit.msshop.shared.adapter.in.web.request.ChangeRequest;

@Component
@RequiredArgsConstructor
public class BrandWebMapper {

    private final BrandLogoUrlResolver urlResolver;

    public BrandId toBrandId(
            final UUID id) {
        return new BrandId(id);
    }

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

    public BrandResponse toResponse(
            final BrandView view) {
        return new BrandResponse(
                view.id(),
                view.name(),
                this.urlResolver.toLogoUrlString(view.logoKey()),
                view.version());
    }
}
