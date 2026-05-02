package vn.edu.uit.msshop.product.product.application.service.command;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.RestoreProductCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.RestoreProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadSoftDeletedProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.RestoreVariantsForProductPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductRestored;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;
import vn.edu.uit.msshop.product.shared.event.document.ProductCreatedDocument;
import vn.edu.uit.msshop.product.shared.event.document.VariantCreatedDocument;
import vn.edu.uit.msshop.product.shared.event.repository.ProductCreatedRepository;

@Service
@RequiredArgsConstructor
public class RestoreProductService implements RestoreProductUseCase {
    private final LoadSoftDeletedProductPort loadSoftDeletedPort;
    private final UpdateProductPort updatePort;
    private final RestoreVariantsForProductPort restoreVariantsForProductPort;
    private final PublishProductEventPort eventPort;
    private final ProductCreatedRepository productCreatedRepo;
    private final vn.edu.uit.msshop.product.shared.application.port.out.PublishProductEventPort publishProductEventPort;

    @Override
    @Transactional
    public void restore(
            final RestoreProductCommand command) {
        final var productId = command.id();
        final var product = this.loadSoftDeletedPort
                .loadSoftDeletedById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

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
                null);
        final var saved = this.updatePort.update(next);

        final var manifestIds = saved.getVariants().values().stream()
                .map(ProductVariant::id)
                .toList();
        this.restoreVariantsForProductPort.restoreByVariantIds(manifestIds);
        List<VariantCreatedDocument> variantCreateds = saved.getVariants().values().stream().map(item->new VariantCreatedDocument(item.id().value(), item.price().value(),getTraits(item),"")).toList();

        ProductCreatedDocument productCreatedDocument = new ProductCreatedDocument(UUID.randomUUID(), saved.getId().value(), saved.getName().value(), variantCreateds, "PENDING", 
            0, Instant.now(), null, null);
        final var savedEvent = productCreatedRepo.save(productCreatedDocument);
        publishProductEventPort.publishProductCreated(savedEvent);

        this.eventPort.publish(new ProductRestored(saved.getId()));
    }
    private List<String> getTraits(ProductVariant v) {
        return v.traits().values().stream().map(item->item.value()).toList();
    }

}
