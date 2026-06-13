package vn.edu.uit.msshop.product.variant.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.variant.adapter.in.web.request.UpdateVariantImageRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantImageResponse;
import vn.edu.uit.msshop.product.variant.application.dto.command.image.VariantImageDeletionByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.image.VariantImageUpdateByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantImageView;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Component
public class VariantImageWebMapper {
    public VariantId toVariantId(
            final UUID id) {
        return new VariantId(id);
    }

    public VariantImageResponse toImageResponse(
            final VariantImageView view) {
        return new VariantImageResponse(
                view.id(),
                view.imageKey(),
                view.version());
    }

    public VariantImageUpdateByIdCommand toUpdateImageCommand(
            final UUID variantId,
            final UpdateVariantImageRequest request) {
        return new VariantImageUpdateByIdCommand(
                variantId,
                request.newImageKey(),
                request.version());
    }

    public VariantImageDeletionByIdCommand toDeleteImageCommand(
            final UUID variantId,
            final long variantVersion) {
        return new VariantImageDeletionByIdCommand(
                variantId,
                variantVersion);
    }
}
