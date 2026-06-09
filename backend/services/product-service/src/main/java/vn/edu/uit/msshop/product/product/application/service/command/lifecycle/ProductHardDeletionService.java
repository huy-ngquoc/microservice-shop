package vn.edu.uit.msshop.product.product.application.service.command.lifecycle;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductHardDeletionCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductHardDeletionUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.ProductEventPublicationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductSoldCountDeletionByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductStockCountDeletionByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductDeletionByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductSoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.command.ProductRatingDeletionPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkHardDeletionForProductPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductHardDeletedEvent;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

// TODO: delete image as well.
@Service
@RequiredArgsConstructor
public class ProductHardDeletionService
        implements
        ProductHardDeletionUseCase {
    private final ProductSoftDeletedLookupByIdPort softDeletedLookupByIdPort;
    private final ProductDeletionByIdPort deletionByIdPort;
    private final ProductSoldCountDeletionByIdPort soldCountDeletionByIdPort;
    private final ProductStockCountDeletionByIdPort stockCountDeletionByIdPort;
    private final ProductRatingDeletionPort ratingDeletionPort;

    private final ProductVariantBulkHardDeletionForProductPort variantBulkHardDeleteByIdsPort;

    private final ProductEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    public void hardDelete(
            ProductHardDeletionCommand command) {
        final var productId = command.id();
        final var product = this.softDeletedLookupByIdPort.loadSoftDeletedById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = product.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        this.deletionByIdPort.deleteById(productId);
        this.soldCountDeletionByIdPort.deleteById(productId);
        this.stockCountDeletionByIdPort.deleteById(productId);
        this.ratingDeletionPort.deleteById(productId);

        this.variantBulkHardDeleteByIdsPort.purgeByProductId(productId);

        this.eventPublicationPort.publishEvent(new ProductHardDeletedEvent(productId));
    }
}
