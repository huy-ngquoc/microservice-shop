package vn.edu.uit.msshop.product.variant.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.variant.adapter.in.web.request.VariantImageUpdateRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantImageResponse;
import vn.edu.uit.msshop.product.variant.application.dto.command.image.VariantImageDeletionByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.image.VariantImageUpdateByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.lookup.VariantImageActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantImageView;

@Component
public class VariantImageWebMapper {

    public VariantImageResponse toResponse(
            final VariantImageView view) {
        return new VariantImageResponse(
                view.id(),
                view.imageKey(),
                view.version());
    }

    public VariantImageActiveLookupByIdQuery toActiveLookupByIdQuery(
            final UUID variantId) {
        return new VariantImageActiveLookupByIdQuery(
                variantId);
    }

    public VariantImageUpdateByIdCommand toUpdateByIdCommand(
            final UUID variantId,
            final VariantImageUpdateRequest request) {
        return new VariantImageUpdateByIdCommand(
                variantId,
                request.newImageKey(),
                request.version());
    }

    public VariantImageDeletionByIdCommand toDeleteByIdCommand(
            final UUID variantId,
            final long variantVersion) {
        return new VariantImageDeletionByIdCommand(
                variantId,
                variantVersion);
    }
}
