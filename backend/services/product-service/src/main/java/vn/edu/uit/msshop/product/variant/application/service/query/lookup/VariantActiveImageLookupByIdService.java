package vn.edu.uit.msshop.product.variant.application.service.query.lookup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantImageView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.query.lookup.VariantActiveImageLookupByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
class VariantActiveImageLookupByIdService
        implements VariantActiveImageLookupByIdUseCase {

    private final LoadVariantPort loadPort;
    private final VariantViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public VariantImageView findImageById(
            final VariantId id) {
        return this.loadPort.loadById(id)
                .map(this.mapper::toImageView)
                .orElseThrow(() -> new VariantNotFoundException(id));
    }
}
