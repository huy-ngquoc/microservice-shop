package vn.edu.uit.msshop.product.variant.application.service.command;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.DeleteProductImageCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;

import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductImageKey;
import vn.edu.uit.msshop.product.variant.application.dto.command.DeleteImageCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.DeleteImageUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.image.VariantImageStoragePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateVariantPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteImageService implements DeleteImageUseCase {
    private final LoadVariantPort loadVariantPort;
    
    private final UpdateVariantPort saveVariantPort;
    private final vn.edu.uit.msshop.product.product.application.port.in.command.DeleteImageUseCase deleteImageUseCase;
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
    public void deleteImage(DeleteImageCommand command) {
       Variant variant = loadVariantPort.loadById(command.variantId())
        .orElseThrow(() -> new VariantNotFoundException(command.variantId()));
        
        
        if(command.version()!=variant.getVersion().value()) throw new OptimisticLockException(variant.getVersion().value(), command.version());
        if(variant.getImageKey()==null||variant.getImageKey().value()==null) {
            throw new RuntimeException("Variant does not have a key yet");
        }
        String removedKey = variant.getImageKey().value();
        final Variant toSaveVariant = variant.updateImageKey(null);
        final var deleteCommand = new DeleteProductImageCommand(new ProductId(variant.getProductId().value()),new ProductImageKey(removedKey));
        saveVariantPort.update(toSaveVariant);
        deleteImageUseCase.deleteImage(deleteCommand);
        variantImageStoragePort.deleteImage(new VariantImageKey(removedKey));
    }



}
