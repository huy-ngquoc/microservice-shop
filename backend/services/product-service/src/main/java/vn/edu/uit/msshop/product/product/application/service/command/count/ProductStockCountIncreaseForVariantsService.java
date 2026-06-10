package vn.edu.uit.msshop.product.product.application.service.command.count;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductStockCountsForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductStockCountIncreaseForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductStockCountBulkIncreationPort;

@Service
@RequiredArgsConstructor
public class ProductStockCountIncreaseForVariantsService
        implements ProductStockCountIncreaseForVariantsUseCase {
    private final ProductStockCountBulkIncreationPort bulkIncreationPort;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT,
                            allEntries = true),
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT_LIST,
                            allEntries = true)
            })
    public void increase(
            final IncreaseProductStockCountsForVariantsCommand command) {
        this.bulkIncreationPort.increaseAll(command.incrementById());
    }
}
