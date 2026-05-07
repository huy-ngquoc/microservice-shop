package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductStockCountsForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.IncreaseProductStockCountsForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.IncreaseAllProductStockCountsPort;

@Service
@RequiredArgsConstructor
public class IncreaseProductStockCountsForVariantsService
        implements IncreaseProductStockCountsForVariantsUseCase {
    private final IncreaseAllProductStockCountsPort increaseAllPort;

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
    public void execute(
            final IncreaseProductStockCountsForVariantsCommand command) {
        this.increaseAllPort.increaseAll(command.incrementById());
    }
}
