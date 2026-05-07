package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductSoldCountsForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.IncreaseProductSoldCountsForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.IncreaseAllProductSoldCountsPort;

@Service
@RequiredArgsConstructor
public class IncreaseProductSoldCountsForVariantsService
        implements IncreaseProductSoldCountsForVariantsUseCase {
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
    public void execute(
            final IncreaseProductSoldCountsForVariantsCommand command) {
        this.increaseAllSoldCountsPort.increaseAll(command.incrementById());
    }
}
