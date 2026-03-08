package vn.edu.uit.msshop.product.brand.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.brand.adapter.in.web.request.CreateBrandRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.UpdateBrandInfoRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandLogoResponse;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandResponse;
import vn.edu.uit.msshop.product.brand.application.dto.command.CreateBrandCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.UpdateBrandInfoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.UpdateBrandLogoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.query.BrandLogoView;
import vn.edu.uit.msshop.product.brand.application.dto.query.BrandView;
import vn.edu.uit.msshop.product.brand.domain.model.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.BrandName;
import vn.edu.uit.msshop.product.shared.adapter.in.web.request.ChangeRequest;

@Component
public class BrandWebMapper {
    public CreateBrandCommand toCommand(
            final CreateBrandRequest request) {
        final var name = new BrandName(request.name());

        return new CreateBrandCommand(name);
    }

    public UpdateBrandInfoCommand toCommand(
            final UUID id,
            final UpdateBrandInfoRequest request) {
        final var brandId = new BrandId(id);

        final var name = ChangeRequest.toChange(request.name(), BrandName::new);

        return new UpdateBrandInfoCommand(
                brandId,
                name);
    }

    public UpdateBrandLogoCommand toCommand(
            final UUID id,
            final byte[] bytes,
            final String originalFilename,
            final String contentType) {
        return new UpdateBrandLogoCommand(
                new BrandId(id),
                bytes,
                originalFilename,
                contentType);
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
                view.logoUrl());
    }

    public BrandLogoResponse toResponse(
            final BrandLogoView view) {
        return new BrandLogoResponse(
                view.url(),
                view.width(),
                view.height());
    }
}
