package vn.edu.uit.msshop.product.variant.adapter.in.web.mapper;

import java.util.UUID;
import java.util.List;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.shared.adapter.in.web.request.ChangeRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.CreateVariantRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.UpdateVariantInfoRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantResponse;
import vn.edu.uit.msshop.product.variant.application.dto.command.CreateVariantCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantInfoCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantImageKey;
import vn.edu.uit.msshop.product.variant.domain.model.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTrait;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTraits;

@Component
public class VariantWebMapper {
    public CreateVariantCommand toCommand(
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

    public UpdateVariantInfoCommand toCommand(
            final UUID id,
            final UpdateVariantInfoRequest request) {
        final var variantId = new VariantId(id);
        final var price = ChangeRequest.toChange(request.price(), VariantPrice::new);
        final var traits = ChangeRequest.toChange(request.traits(), VariantWebMapper::toVariantTraits);

        return new UpdateVariantInfoCommand(
                variantId,
                price,
                traits);
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
                view.imageKey(),
                view.price(),
                view.sold());
    }

    private static VariantTraits toVariantTraits(
            final List<String> rawTraits) {
        final var traits = rawTraits.stream()
                .map(VariantTrait::new)
                .toList();

        return new VariantTraits(traits);
    }
}
