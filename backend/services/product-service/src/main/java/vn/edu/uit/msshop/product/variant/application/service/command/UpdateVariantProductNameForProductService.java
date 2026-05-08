package vn.edu.uit.msshop.product.variant.application.service.command;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantProductNameForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.UpdateVariantProductNameForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantsProductNameForProductPort;

@Service
@RequiredArgsConstructor
public class UpdateVariantProductNameForProductService
        implements UpdateVariantProductNameForProductUseCase {
    private final UpdateAllVariantsProductNameForProductPort updatePort;

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
    public void execute(
            final UpdateVariantProductNameForProductCommand command) {
        this.updatePort.updateProductNameByProductId(
                command.productId(),
                command.productName());
    }
}
