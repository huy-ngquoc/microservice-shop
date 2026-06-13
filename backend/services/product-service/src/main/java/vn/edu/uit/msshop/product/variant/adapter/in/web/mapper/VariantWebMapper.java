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
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.UpdateVariantInfoRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantResponse;
import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantHardDeletionByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantRestorationByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantSoftDeletionByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantInfoUpdateByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.ListVariantsQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

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
        final var pageRequest = new PageRequestDto(
                page,
                size,
                sortBy,
                direction);

        final List<String> targets;
        if (rawTargets == null) {
            targets = List.of();
        } else {
            targets = List.copyOf(rawTargets);
        }

        return new ListVariantsQuery(pageRequest, targets);
    }

    public Set<VariantId> toVariantIds(
            final FindVariantsByIdsRequest request) {
        return request.ids().stream()
                .map(VariantId::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    public VariantRestorationByIdCommand toRestoreCommand(
            final UUID variantId,
            final long variantVersion) {
        return new VariantRestorationByIdCommand(
                variantId,
                variantVersion);
    }

    public VariantInfoUpdateByIdCommand toUpdateInfoCommand(
            final UUID variantId,
            final UpdateVariantInfoRequest request) {
        final var priceChange = ChangeRequest.toChange(request.price());
        final var traitListChange = ChangeRequest.toChange(request.traits());
        final var targetListChange = ChangeRequest.toChange(request.targets());

        return new VariantInfoUpdateByIdCommand(
                variantId,
                priceChange,
                traitListChange,
                targetListChange,
                request.version());
    }

    public VariantSoftDeletionByIdCommand toSoftDeleteCommand(
            final UUID variantId,
            final long variantVersion) {
        return new VariantSoftDeletionByIdCommand(
                variantId,
                variantVersion);
    }

    public VariantHardDeletionByIdCommand toHardDeleteCommand(
            final UUID variantId,
            final long variantVersion) {
        return new VariantHardDeletionByIdCommand(
                variantId,
                variantVersion);
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
}
