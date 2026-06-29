package vn.edu.uit.msshop.product.variant.adapter.out.reconciliation;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.reconciliation.ProductReconciliationActiveIdListingQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.reconciliation.ProductReconciliationActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.reconciliation.ProductReconciliationSoftDeletedIdListingQuery;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductReconciliationView;
import vn.edu.uit.msshop.product.product.application.port.in.query.reconciliation.ProductReconciliationActiveIdListingUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.reconciliation.ProductReconciliationActiveLookupByIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.reconciliation.ProductReconciliationSoftDeletedIdListingUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.reconciliation.VariantProductReconciliationActiveIdListingPort;
import vn.edu.uit.msshop.product.variant.application.port.out.reconciliation.VariantProductReconciliationActiveLookupByProductIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.reconciliation.VariantProductReconciliationSoftDeletedIdListingPort;
import vn.edu.uit.msshop.product.variant.domain.model.reconciliation.VariantProductReconciliationSnapshot;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductName;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Component
@RequiredArgsConstructor
class VariantProductReconciliationAdapter
        implements
        VariantProductReconciliationActiveIdListingPort,
        VariantProductReconciliationSoftDeletedIdListingPort,
        VariantProductReconciliationActiveLookupByProductIdPort {

    private final ProductReconciliationActiveIdListingUseCase activeIdListingUseCase;
    private final ProductReconciliationSoftDeletedIdListingUseCase softDeletedIdListingUseCase;
    private final ProductReconciliationActiveLookupByIdUseCase activeLookupByIdUseCase;

    @Override
    public PageResponseDto<UUID> listActiveIds(
            final PageRequestDto pageRequest) {
        final var query = new ProductReconciliationActiveIdListingQuery(pageRequest);
        return this.activeIdListingUseCase.list(query);
    }

    @Override
    public PageResponseDto<UUID> listSoftDeletedIds(
            final PageRequestDto pageRequest) {
        final var query = new ProductReconciliationSoftDeletedIdListingQuery(pageRequest);
        return this.softDeletedIdListingUseCase.list(query);
    }

    @Override
    public Optional<VariantProductReconciliationSnapshot> findActiveByProductId(
            final VariantProductId productId) {
        final var query = new ProductReconciliationActiveLookupByIdQuery(productId.value());
        return this.activeLookupByIdUseCase.find(query)
                .map(VariantProductReconciliationAdapter::toSnapshot);
    }

    private static VariantProductReconciliationSnapshot toSnapshot(
            final ProductReconciliationView view) {
        final var productId = new VariantProductId(view.productId());
        final var name = new VariantProductName(view.name());
        final var variantIdSet = view.variantIdSet().stream()
                .map(VariantId::new)
                .collect(Collectors.toUnmodifiableSet());
        return new VariantProductReconciliationSnapshot(
                productId,
                name,
                variantIdSet);
    }
}
