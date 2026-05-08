package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.HardDeleteProductCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.HardDeleteProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.DeleteProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.DeleteProductRatingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.DeleteProductSoldCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.DeleteProductStockCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadSoftDeletedProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.HardDeleteAllProductVariantsPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductPurged;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

// TODO: delete image as well.
@Service
@RequiredArgsConstructor
public class HardDeleteProductService implements HardDeleteProductUseCase {
    private final LoadSoftDeletedProductPort loadSoftDeletedPort;
    private final DeleteProductPort deletePort;
    private final DeleteProductSoldCountPort deleteSoldCountPort;
    private final DeleteProductStockCountPort deleteStockCountPort;
    private final DeleteProductRatingPort deleteRatingPort;
    private final HardDeleteAllProductVariantsPort hardDeleteAllVariantsPort;
    private final PublishProductEventPort eventPort;

    @Override
    @Transactional
    public void purge(
            HardDeleteProductCommand command) {
        final var productId = command.id();
        final var product = this.loadSoftDeletedPort.loadSoftDeletedById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = product.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        this.deletePort.deleteById(productId);
        this.deleteSoldCountPort.deleteById(productId);
        this.deleteStockCountPort.deleteById(productId);
        this.deleteRatingPort.deleteById(productId);

        this.hardDeleteAllVariantsPort.purgeByProductId(productId);

        this.eventPort.publish(new ProductPurged(productId));
    }
}
