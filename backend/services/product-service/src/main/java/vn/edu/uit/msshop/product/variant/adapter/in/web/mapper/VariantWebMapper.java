package vn.edu.uit.msshop.product.variant.adapter.in.web.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.shared.adapter.in.web.request.ChangeRequest;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.VariantInfoUpdateRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantResponse;
import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantHardDeletionByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantRestorationByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantSoftDeletionByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.listing.VariantActiveListingQuery;
import vn.edu.uit.msshop.product.variant.application.dto.query.lookup.VariantActiveBulkLookupByIdsQuery;
import vn.edu.uit.msshop.product.variant.application.dto.query.lookup.VariantSoftDeletedLookupByIdQuery;
import vn.edu.uit.msshop.product.variant.application.dto.query.lookup.VariantActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantInfoUpdateByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Component
public class VariantWebMapper {

    public List<VariantResponse> toListResponse(
            final Collection<VariantView> views) {
        return views.stream()
                .map(this::toResponse)
                .toList();
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

    public VariantActiveListingQuery toActiveListingQuery(
            int page,

            int size,

            @Nullable
            String sortBy,

            PageRequestDto.Direction direction,

            @Nullable
            List<String> rawTargetList) {
        final var pageRequest = new PageRequestDto(
                page,
                size,
                sortBy,
                direction);

        final List<String> targetList;
        if (rawTargetList == null) {
            targetList = List.of();
        } else {
            targetList = List.copyOf(rawTargetList);
        }

        return new VariantActiveListingQuery(
                pageRequest,
                targetList);
    }

    public VariantActiveBulkLookupByIdsQuery toActiveBulkLookupByIdsQuery(
            final Set<UUID> variantIdSet) {
        return new VariantActiveBulkLookupByIdsQuery(
                variantIdSet);
    }

    public VariantActiveLookupByIdQuery toActiveLookupByIdQuery(
            final UUID variantId) {
        return new VariantActiveLookupByIdQuery(
                variantId);
    }

    public VariantSoftDeletedLookupByIdQuery toSoftDeletedLookupByIdQuery(
            final UUID variantId) {
        return new VariantSoftDeletedLookupByIdQuery(
                variantId);
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
            final VariantInfoUpdateRequest request) {
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
}
