package vn.edu.uit.msshop.product.product.application.service.command;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.SoftDeleteProductCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.SoftDeleteProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.SoftDeleteVariantsForProductPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductSoftDeleted;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductDeletionTime;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;
import vn.edu.uit.msshop.product.shared.event.document.ProductDeletedDocument;
import vn.edu.uit.msshop.product.shared.event.repository.ProductDeletedRepository;

@Service
@RequiredArgsConstructor
public class SoftDeleteProductService implements SoftDeleteProductUseCase {
    private final LoadProductPort loadPort;
    private final UpdateProductPort updatePort;
    private final SoftDeleteVariantsForProductPort softDeleteVariantsForProductPort;
    private final PublishProductEventPort eventPort;
    private final ProductDeletedRepository productDeletedRepo;
    private final vn.edu.uit.msshop.product.shared.application.port.out.PublishProductEventPort publishProductEventPort;
    @Override
    @Transactional
    public void delete(
            final SoftDeleteProductCommand command) {
        final var productId = command.id();
        final var product = this.loadPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // TODO: duplicate code across services, move it to somewhere to reuse
        final var expectedVersion = command.expectedVersion();
        final var currentVersion = product.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var next = new Product(
                product.getId(),
                product.getName(),
                product.getCategoryId(),
                product.getBrandId(),
                product.getConfiguration(),
                product.getImageKeys(),
                product.getVersion(),
                ProductDeletionTime.now());
        final var saved = this.updatePort.update(next);

        this.softDeleteVariantsForProductPort
                .deleteByProductId(saved.getId());
        ProductDeletedDocument eventDocument = new ProductDeletedDocument(UUID.randomUUID(), saved.getId().value(), "PENDING", 0, Instant.now(), null, null);
        ProductDeletedDocument savedEvent = productDeletedRepo.save(eventDocument);
        publishProductEventPort.publishProductDeleted(savedEvent);

        this.eventPort.publish(new ProductSoftDeleted(saved.getId()));
    }
}
