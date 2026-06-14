package vn.edu.uit.msshop.product.variant.application.service.command.sync;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantProductNameBulkUpdateForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantProductNameBulkUpdateForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantProductNameBulkUpdateByProductIdPort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductName;

@Service
@RequiredArgsConstructor
class VariantProductNameBulkUpdateForProductService
        implements VariantProductNameBulkUpdateForProductUseCase {

    private final VariantProductNameBulkUpdateByProductIdPort productNameBulkUpdateByProductIdPort;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.VARIANT,
                            allEntries = true),
                    @CacheEvict(
                            cacheNames = CacheNames.VARIANT_LIST,
                            allEntries = true)
            })
    public void updateAll(
            final VariantProductNameBulkUpdateForProductCommand cmd) {
        final var productId = new VariantProductId(cmd.productId());
        final var productName = new VariantProductName(cmd.productName());

        this.productNameBulkUpdateByProductIdPort.updateProductNameByProductId(
                productId,
                productName);
    }
}
