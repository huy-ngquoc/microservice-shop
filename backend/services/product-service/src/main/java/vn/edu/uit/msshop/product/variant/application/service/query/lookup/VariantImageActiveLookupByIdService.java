package vn.edu.uit.msshop.product.variant.application.service.query.lookup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.query.lookup.VariantImageActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantImageView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.query.lookup.VariantImageActiveLookupByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveLookupByIdPort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
class VariantImageActiveLookupByIdService
        implements VariantImageActiveLookupByIdUseCase {

    private final VariantActiveLookupByIdPort activeLookupByIdPort;
    private final VariantViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public VariantImageView find(
            final VariantImageActiveLookupByIdQuery query) {
        final var variantId = new VariantId(query.variantId());
        return this.activeLookupByIdPort.loadActiveById(variantId)
                .map(this.mapper::toImageView)
                .orElseThrow(() -> new VariantNotFoundException(variantId));
    }
}
