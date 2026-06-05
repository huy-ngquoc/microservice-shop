package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.AddImageCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.AddProductImageUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductImageKey;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddProductImageService implements AddProductImageUseCase{
    private final LoadProductPort loadPort;
    private final UpdateProductPort updatePort;
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
    public void addImage(AddImageCommand command) {
        Product product = loadPort.loadById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));
        final Product toSaveProduct = product.addKey(command.key());
            updatePort.update(toSaveProduct);
    }

}
