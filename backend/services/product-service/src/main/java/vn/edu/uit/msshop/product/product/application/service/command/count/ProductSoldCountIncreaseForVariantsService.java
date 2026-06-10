package vn.edu.uit.msshop.product.product.application.service.command.count;

import java.util.HashMap;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductSoldCountIncreaseForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductSoldCountIncreaseForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductSoldCountBulkIncreationPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
public class ProductSoldCountIncreaseForVariantsService
        implements ProductSoldCountIncreaseForVariantsUseCase {
    private final ProductSoldCountBulkIncreationPort bulkIncreationPort;

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
            final ProductSoldCountIncreaseForVariantsCommand cmd) {
        final var incrementByProductId = HashMap.<ProductId, Integer>newHashMap(
                cmd.incrementById().size());

        for (final var entry : cmd.incrementById().entrySet()) {
            final var productId = new ProductId(entry.getKey());
            final var increment = entry.getValue();

            incrementByProductId.put(productId, increment);
        }

        this.bulkIncreationPort.increaseAll(incrementByProductId);
    }
}
