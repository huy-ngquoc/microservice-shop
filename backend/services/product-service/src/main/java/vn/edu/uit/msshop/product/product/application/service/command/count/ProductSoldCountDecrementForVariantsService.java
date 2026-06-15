package vn.edu.uit.msshop.product.product.application.service.command.count;

import java.util.HashMap;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductSoldCountDecreaseForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductSoldCountDecrementForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductSoldCountBulkDecrementPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
class ProductSoldCountDecrementForVariantsService
        implements ProductSoldCountDecrementForVariantsUseCase {

    private final ProductSoldCountBulkDecrementPort bulkDecrementPort;

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
            ProductSoldCountDecreaseForVariantsCommand cmd) {
        final var incrementByProductId = HashMap.<ProductId, Integer>newHashMap(
                cmd.decrementById().size());

        for (final var entry : cmd.decrementById().entrySet()) {
            final var productId = new ProductId(entry.getKey());
            final var decrement = entry.getValue();

            incrementByProductId.put(productId, decrement);
        }

        this.bulkDecrementPort.decreaseAll(incrementByProductId);
    }
}
