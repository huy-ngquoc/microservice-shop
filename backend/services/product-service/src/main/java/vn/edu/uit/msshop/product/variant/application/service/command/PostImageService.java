package vn.edu.uit.msshop.product.variant.application.service.command;

import java.io.IOException;
import java.util.Objects;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductImageKey;
import vn.edu.uit.msshop.product.variant.application.dto.command.PostImageCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.PostImageUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.image.VariantImageStoragePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateVariantPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostImageService implements PostImageUseCase {
    private final LoadVariantPort loadVariantPort;
    private final LoadProductPort loadProductPort;
    private final UpdateVariantPort saveVariantPort;
    private final UpdateProductPort saveProductPort;
    private final VariantImageStoragePort variantImageStoragePort;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.VARIANT,
                            key = "#command.variantId().value()"
                    ),
                            
                    @CacheEvict(
                            cacheNames = CacheNames.VARIANT_LIST,
                            allEntries = true)
            })
    public void postImage(PostImageCommand command) throws IOException {
        Variant variant = loadVariantPort.loadById(command.variantId())
                .orElseThrow(() -> new VariantNotFoundException(command.variantId()));
        
        Product product = loadProductPort.loadById(new ProductId(variant.getProductId().value()))
                .orElseThrow(() -> new ProductNotFoundException(new ProductId(variant.getProductId().value())));
        
        if (command.version() != variant.getVersion().value()) {
            throw new OptimisticLockException(variant.getVersion().value(), command.version());
        }

        // SỬA TẠI ĐÂY: Sử dụng Objects.requireNonNull để bảo vệ biến newPublicId khỏi nguy cơ @Nullable
        String newPublicId = Objects.requireNonNull(
                variantImageStoragePort.postImage(command.image()), 
                "Uploaded image public ID must not be null"
        );
        
        String newKey = extractKey(newPublicId);

        VariantImageKey oldImageKey = variant.getImageKey();

        if (oldImageKey != null && oldImageKey.value() != null && !oldImageKey.value().isEmpty()) {
            String oldKey = oldImageKey.value();
            
            final Product toSaveProduct = product.replaceKey(new ProductImageKey(oldKey), new ProductImageKey(newKey));
            saveProductPort.update(toSaveProduct);
            
            variantImageStoragePort.deleteImage(oldImageKey); 
            
        } else {
            final Product toSaveProduct = product.addKey(new ProductImageKey(newKey));
            saveProductPort.update(toSaveProduct);
        }

        final Variant toSaveVariant = variant.updateImageKey(new VariantImageKey(newKey));
        saveVariantPort.update(toSaveVariant);
    }

    // SỬA TẠI ĐÂY: Tái cấu trúc hàm extractKey để triệt tiêu đường return null mang nhãn @Nullable
    private String extractKey(String publicId) {
        if (publicId.contains("/")) {
            return publicId.substring(publicId.lastIndexOf("/") + 1);
        }
        return publicId;
    }
}