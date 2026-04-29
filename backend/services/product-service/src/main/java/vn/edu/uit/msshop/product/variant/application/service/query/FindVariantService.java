package vn.edu.uit.msshop.product.variant.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.query.FindVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantSoldCountPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantStockCountPort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class FindVariantService implements FindVariantUseCase {
    private final LoadVariantPort loadPort;
    private final LoadVariantSoldCountPort loadSoldCountPort;
    private final LoadVariantStockCountPort loadStockCountPort;
    private final VariantViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public VariantView findById(
            final VariantId id) {
        final var variant = this.loadPort.loadById(id)
                .orElseThrow(() -> new VariantNotFoundException(id));
        final var soldCount = this.loadSoldCountPort.loadByIdOrZero(
                variant.getId(),
                variant.getProductId());
        final var stockCount = this.loadStockCountPort.loadByIdOrZero(
                variant.getId(),
                variant.getProductId());

        return this.mapper.toView(
                variant,
                soldCount,
                stockCount);
    }
}
