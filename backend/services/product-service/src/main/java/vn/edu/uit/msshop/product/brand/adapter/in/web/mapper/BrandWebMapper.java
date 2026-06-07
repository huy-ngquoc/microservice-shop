package vn.edu.uit.msshop.product.brand.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.CreateBrandRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.UpdateBrandInfoRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandResponse;
import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandCreationCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandHardDeletionByIdCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandInfoUpdateByIdCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandRestorationByIdCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandSoftDeletionByIdCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.shared.adapter.in.web.request.ChangeRequest;

@Component
@RequiredArgsConstructor
public class BrandWebMapper {

    private final BrandLogoUrlResolver urlResolver;

    public BrandId toBrandId(
            final UUID id) {
        return new BrandId(id);
    }

    public BrandCreationCommand toCreationCommand(
            final CreateBrandRequest request) {
        return new BrandCreationCommand(
                request.name());
    }

    public BrandInfoUpdateByIdCommand toInfoUpdateByIdCommand(
            final UUID brandId,
            final UpdateBrandInfoRequest request) {
        final var brandNameChange = ChangeRequest.toChange(request.name());
        return new BrandInfoUpdateByIdCommand(
                brandId,
                brandNameChange,
                request.version());
    }

    public BrandSoftDeletionByIdCommand toSoftDeletionByIdCommand(
            final UUID brandId,
            final long brandVersion) {
        return new BrandSoftDeletionByIdCommand(
                brandId,
                brandVersion);
    }

    public BrandRestorationByIdCommand toRestorationByIdCommand(
            final UUID brandId,
            final long brandVersion) {
        return new BrandRestorationByIdCommand(
                brandId,
                brandVersion);
    }

    public BrandHardDeletionByIdCommand toHardDeletionByIdCommand(
            final UUID brandId,
            final long brandVersion) {
        return new BrandHardDeletionByIdCommand(
                brandId,
                brandVersion);
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
