package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.application.dto.command.ReorderImageCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.ReorderImageUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReorderImageService implements ReorderImageUseCase {
    private final LoadProductPort loadPort;
    private final UpdateProductPort updateProductPort;
    @Override
    public void reOrder(ReorderImageCommand command) {
       Product product = loadPort.loadById(command.productId())
        .orElseThrow(() -> new ProductNotFoundException(command.productId()));
        if(command.version()!=product.getVersion().value()) throw new OptimisticLockException(product.getVersion().value(), command.version());
        final Product toSaveProduct = product.reorderImages(command.newIndexes());
        updateProductPort.update(toSaveProduct);
    }
    
}
