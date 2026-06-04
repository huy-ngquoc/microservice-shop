package vn.edu.uit.msshop.product.product.application.service.command.count;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductSoldCountsForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductSoldCountIncreaseForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.IncreaseAllProductSoldCountsPort;

@Service
@RequiredArgsConstructor
public class ProductSoldCountIncreaseForVariantsService
        implements ProductSoldCountIncreaseForVariantsUseCase {
    private final IncreaseAllProductSoldCountsPort increaseAllSoldCountsPort;

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
            final IncreaseProductSoldCountsForVariantsCommand command) {
        this.increaseAllSoldCountsPort.increaseAll(command.incrementById());
    }
}
