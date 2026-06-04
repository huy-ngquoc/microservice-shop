package vn.edu.uit.msshop.product.product.application.service.command.count;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.DecreaseProductSoldCountsForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductSoldCountDecreaseForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.DecreaseAllProductSoldCountsPort;

@Service
@RequiredArgsConstructor
public class ProductSoldCountDecreaseForVariantsService
        implements ProductSoldCountDecreaseForVariantsUseCase {
    private final DecreaseAllProductSoldCountsPort decreaseAllPort;

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
    public void decrease(
            DecreaseProductSoldCountsForVariantsCommand command) {
        this.decreaseAllPort.decreaseAll(command.decrementById());
    }
}
