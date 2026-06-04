package vn.edu.uit.msshop.product.product.application.service.query.existence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.CheckProductExistsByBrandUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.CheckProductExistsByCategoryUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.CheckProductExistsByVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.CheckProductExistsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.CheckProductExistsByBrandPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.CheckProductExistsByCategoryPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.CheckProductExistsByVariantPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.CheckProductExistsPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;

@Service
@RequiredArgsConstructor
public class ProductActiveExistenceCheckService
        implements
        CheckProductExistsUseCase,
        CheckProductExistsByBrandUseCase,
        CheckProductExistsByCategoryUseCase,
        CheckProductExistsByVariantUseCase {
    private final CheckProductExistsPort checkExistsPort;
    private final CheckProductExistsByBrandPort checkExistsByBrandPort;
    private final CheckProductExistsByCategoryPort checkExistsByCategoryPort;
    private final CheckProductExistsByVariantPort checkExistsByVariantPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsById(
            final ProductId id) {
        return this.checkExistsPort.existsById(id);
    }

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsByBrandId(
            final ProductBrandId brandId) {
        return this.checkExistsByBrandPort.existsByBrandId(brandId);
    }

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsByCategoryId(
            final ProductCategoryId categoryId) {
        return this.checkExistsByCategoryPort.existsByCategoryId(categoryId);
    }

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsByVariantId(
            final ProductVariantId variantId) {
        return this.checkExistsByVariantPort.existsByVariantId(variantId);
    }
}
