package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.DecreaseProductSoldCountsForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.DecreaseProductSoldCountsForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.DecreaseAllProductSoldCountsPort;

@Service
@RequiredArgsConstructor
public class DecreaseProductSoldCountsForVariantsService
        implements DecreaseProductSoldCountsForVariantsUseCase {
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
    public void execute(
            DecreaseProductSoldCountsForVariantsCommand command) {
        this.decreaseAllPort.decreaseAll(command.decrementById());
    }
}
