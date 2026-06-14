package vn.edu.uit.msshop.product.variant.application.service.query.lookup;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.query.lookup.VariantActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.query.lookup.VariantActiveLookupByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantSoldCountLookupByIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantStockCountLookupByIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveLookupByIdPort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
class VariantActiveLookupByIdService
        implements VariantActiveLookupByIdUseCase {

    private final VariantActiveLookupByIdPort activeLookupByIdPort;
    private final VariantSoldCountLookupByIdPort soldCountLookupByIdPort;
    private final VariantStockCountLookupByIdPort stockCountLookupByIdPort;

    private final VariantViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.VARIANT,
            key = "#query.variantId()")
    public VariantView find(
            final VariantActiveLookupByIdQuery query) {
        final var variantId = new VariantId(query.variantId());

        final var variant = this.activeLookupByIdPort.loadActiveById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));
        final var soldCount = this.soldCountLookupByIdPort.loadByIdOrZero(
                variant.getId(),
                variant.getProductId());
        final var stockCount = this.stockCountLookupByIdPort.loadByIdOrZero(
                variant.getId(),
                variant.getProductId());

        return this.mapper.toView(
                variant,
                soldCount,
                stockCount);
    }
}
