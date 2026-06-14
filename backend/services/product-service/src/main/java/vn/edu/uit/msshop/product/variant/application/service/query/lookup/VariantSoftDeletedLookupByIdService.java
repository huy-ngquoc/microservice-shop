package vn.edu.uit.msshop.product.variant.application.service.query.lookup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.query.lookup.VariantSoftDeletedLookupByIdQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.query.lookup.VariantSoftDeletedLookupByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantSoldCountLookupByVariantIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantStockCountLookupByVariantIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantSoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
class VariantSoftDeletedLookupByIdService
        implements VariantSoftDeletedLookupByIdUseCase {

    private final VariantSoftDeletedLookupByIdPort softDeletedLookupByIdPort;
    private final VariantSoldCountLookupByVariantIdPort soldCountLookupByIdPort;
    private final VariantStockCountLookupByVariantIdPort stockCountLookupByIdPort;

    private final VariantViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public VariantView find(
            final VariantSoftDeletedLookupByIdQuery query) {
        final var variantId = new VariantId(query.variantId());

        final var variant = this.softDeletedLookupByIdPort.loadSoftDeletedById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));
        final var soldCount = this.soldCountLookupByIdPort.loadByVariantIdOrZero(
                variant.getId(),
                variant.getProductId());
        final var stockCount = this.stockCountLookupByIdPort.loadByVariantIdOrZero(
                variant.getId(),
                variant.getProductId());

        return this.mapper.toView(
                variant,
                soldCount,
                stockCount);
    }
}
