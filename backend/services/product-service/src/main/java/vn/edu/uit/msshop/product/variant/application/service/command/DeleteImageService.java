package vn.edu.uit.msshop.product.variant.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
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
    private final LoadProductPort loadProductPort;
    private final UpdateVariantPort saveVariantPort;
    private final UpdateProductPort saveProductPort;
    private final VariantImageStoragePort variantImageStoragePort;
    @Override
    @Transactional
    public void deleteImage(DeleteImageCommand command) {
       Variant variant = loadVariantPort.loadById(command.variantId())
        .orElseThrow(() -> new VariantNotFoundException(command.variantId()));
        
        Product product = loadProductPort.loadById(new ProductId(variant.getProductId().value()))
        .orElseThrow(() -> new ProductNotFoundException(new ProductId(variant.getProductId().value())));
        if(command.version()!=variant.getVersion().value()) throw new OptimisticLockException(variant.getVersion().value(), command.version());
        if(variant.getImageKey()==null||variant.getImageKey().value()==null) {
            throw new RuntimeException("Variant does not have a key yet");
        }
        String removedKey = variant.getImageKey().value();
        final Variant toSaveVariant = variant.updateImageKey(new VariantImageKey(null));
        final Product toSaveProduct = product.removeKey(new ProductImageKey(removedKey));
        saveVariantPort.update(toSaveVariant);
        saveProductPort.update(toSaveProduct);
        variantImageStoragePort.deleteImage(new VariantImageKey(removedKey));
    }



}
