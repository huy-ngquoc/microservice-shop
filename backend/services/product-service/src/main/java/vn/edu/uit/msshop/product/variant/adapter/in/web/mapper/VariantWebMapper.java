package vn.edu.uit.msshop.product.variant.adapter.in.web.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.shared.adapter.in.web.request.ChangeRequest;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.FindVariantsByIdsRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.UpdateVariantImageRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.UpdateVariantInfoRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantImageResponse;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantResponse;
import vn.edu.uit.msshop.product.variant.application.dto.command.DeleteVariantImageCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.HardDeleteVariantCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.RestoreVariantCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.SoftDeleteVariantCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantImageCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantInfoCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.ListVariantsQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantImageView;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTarget;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTargets;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantVersion;

@Component
public class VariantWebMapper {
    public ListVariantsQuery toListQuery(
            int page,

            int size,

            @Nullable
            String sortBy,

            PageRequestDto.Direction direction,

            @Nullable
            List<String> rawTargets) {
        final var pageRequest = new PageRequestDto(page, size, sortBy, direction);

        final List<VariantTarget> targets;
        if (rawTargets == null) {
            targets = List.of();
        } else {
            targets = rawTargets.stream()
                    .map(VariantTarget::new)
                    .toList();
        }

        return new ListVariantsQuery(
                pageRequest,
                targets);
    }

    public Set<VariantId> toVariantIds(
            final FindVariantsByIdsRequest request) {
        return request.ids().stream()
                .map(VariantId::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    public RestoreVariantCommand toRestoreCommand(
            final UUID id,
            final long expectedVersion) {
        final var variantId = new VariantId(id);
        final var version = new VariantVersion(expectedVersion);

        return new RestoreVariantCommand(
                variantId,
                version);
    }

    public UpdateVariantInfoCommand toUpdateInfoCommand(
            final UUID id,
            final UpdateVariantInfoRequest request) {
        final var variantId = new VariantId(id);
        final var version = new VariantVersion(request.version());

        final var price = ChangeRequest.toChange(request.price(), VariantPrice::new);
        final var traits = ChangeRequest.toChange(request.traits(), VariantTraits::of);
        final var targets = ChangeRequest.toChange(request.targets(), VariantTargets::of);

        return new UpdateVariantInfoCommand(
                variantId,
                price,
                traits,
                targets,
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

    public SoftDeleteVariantCommand toSoftDeleteCommand(
            final UUID id,
            final long expectedVersion) {
        final var variantId = new VariantId(id);
        final var version = new VariantVersion(expectedVersion);

        return new SoftDeleteVariantCommand(
                variantId,
                version);
    }

    public HardDeleteVariantCommand toHardDeleteCommand(
            final UUID id,
            final long expectedVersion) {
        final var variantId = new VariantId(id);
        final var version = new VariantVersion(expectedVersion);

        return new HardDeleteVariantCommand(
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
                view.productName(),
                view.price(),
                view.soldCount(),
                view.stockCount(),
                view.traits(),
                view.targets(),
                view.imageKey(),
                view.version());
    }

    public List<VariantResponse> toListResponse(
            final Collection<VariantView> views) {
        return views.stream()
                .map(this::toResponse)
                .toList();
    }

    public VariantImageResponse toImageResponse(
            final VariantImageView view) {
        return new VariantImageResponse(
                view.id(),
                view.imageKey(),
                view.version());
    }
}
