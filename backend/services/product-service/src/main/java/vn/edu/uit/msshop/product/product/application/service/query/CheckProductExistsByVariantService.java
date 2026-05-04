package vn.edu.uit.msshop.product.product.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.in.query.CheckProductExistsByVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.CheckProductExistsByVariantPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;

@Service
@RequiredArgsConstructor
public class CheckProductExistsByVariantService
        implements CheckProductExistsByVariantUseCase {
    private final CheckProductExistsByVariantPort checkExistsByVariantPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsByVariantId(
            final ProductVariantId variantId) {
        return this.checkExistsByVariantPort.existsByVariantId(variantId);
    }
}
