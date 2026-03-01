package vn.edu.uit.msshop.product.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.adapter.in.web.request.CreateBrandRequest;
import vn.edu.uit.msshop.product.adapter.in.web.request.UpdateBrandInfoRequest;
import vn.edu.uit.msshop.product.adapter.in.web.request.common.ChangeRequest;
import vn.edu.uit.msshop.product.adapter.in.web.response.BrandLogoResponse;
import vn.edu.uit.msshop.product.adapter.in.web.response.BrandResponse;
import vn.edu.uit.msshop.product.application.dto.command.CreateBrandCommand;
import vn.edu.uit.msshop.product.application.dto.command.UpdateBrandInfoCommand;
import vn.edu.uit.msshop.product.application.dto.query.BrandLogoView;
import vn.edu.uit.msshop.product.application.dto.query.BrandView;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandName;

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
