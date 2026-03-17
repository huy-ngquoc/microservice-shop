package vn.edu.uit.msshop.product.variant.adapter.in.web.mapper;

import java.util.UUID;
import java.util.List;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.shared.adapter.in.web.request.ChangeRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.CreateVariantRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.UpdateVariantImageRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.UpdateVariantInfoRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantImageResponse;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantResponse;
import vn.edu.uit.msshop.product.variant.application.dto.command.CreateVariantCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.DeleteVariantImageCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantImageCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantInfoCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantImageView;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantImageKey;
import vn.edu.uit.msshop.product.variant.domain.model.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTrait;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTraits;
import vn.edu.uit.msshop.product.variant.domain.model.VariantVersion;

@Component
public class VariantWebMapper {
    public CreateVariantCommand toCreateCommand(
            final CreateVariantRequest request) {
        final var productId = new VariantProductId(request.productId());
        final var imageKey = new VariantImageKey(request.imageKey());
        final var price = new VariantPrice(request.price());

        final var traitsList = request.traits().stream()
                .map(VariantTrait::new).toList();
        final var traits = new VariantTraits(traitsList);

        return new CreateVariantCommand(
                productId,
                imageKey,
                price,
                traits);
    }

    public UpdateVariantInfoCommand toUpdateInfoCommand(
            final UUID id,
            final UpdateVariantInfoRequest request) {
        final var variantId = new VariantId(id);
        final var version = new VariantVersion(request.version());

        final var price = ChangeRequest.toChange(request.price(), VariantPrice::new);
        final var traits = ChangeRequest.toChange(request.traits(), VariantTraits::of);

        return new UpdateVariantInfoCommand(
                variantId,
                price,
                traits,
                version);
    }

    public UpdateVariantImageCommand toUpdateImageCommand(
            final UUID id,
            final UpdateVariantImageRequest request) {
        final var variantId = new VariantId(id);
        final var imageKey = new VariantImageKey(request.newImageKey());
        final var version = new VariantVersion(request.version());

        return new UpdateVariantImageCommand(
                variantId,
                imageKey,
                version);
    }

    public DeleteVariantImageCommand toDeleteImageCommand(
            final UUID id,
            final long expectedVersion) {
        final var variantId = new VariantId(id);
        final var version = new VariantVersion(expectedVersion);

        return new DeleteVariantImageCommand(
                variantId,
                version);
    }

    public VariantId toVariantId(
            final UUID id) {
        return new VariantId(id);
    }

    public VariantResponse toResponse(
            final VariantView view) {
        return new VariantResponse(
                view.id(),
                view.productId(),
                view.price(),
                view.sold(),
                view.traits(),
                view.imageKey(),
                view.version());
    }

    public VariantImageResponse toImageResponse(
            final VariantImageView view) {
        return new VariantImageResponse(
                view.id(),
                view.imageKey(),
                view.version());
    }
}
