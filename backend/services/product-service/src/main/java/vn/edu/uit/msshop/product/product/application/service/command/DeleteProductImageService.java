package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.DeleteProductImageCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.DeleteImageUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.domain.model.Product;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteProductImageService implements DeleteImageUseCase {
    private final ProductActiveLookupByIdPort loadPort;
    private final ProductUpdatePort updatePort;
    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT,
                            key = "#command.productId().value()"),
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT_LIST,
                            allEntries = true)
            })
    public void deleteImage(DeleteProductImageCommand command) {
        Product product = loadPort.loadById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));
        final Product toSaveProduct = product.removeKey(command.deletedKey());
        updatePort.update(toSaveProduct);
    }

}
