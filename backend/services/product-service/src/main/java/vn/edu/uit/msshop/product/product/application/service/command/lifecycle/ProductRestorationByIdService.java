package vn.edu.uit.msshop.product.product.application.service.command.lifecycle;

import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductRestorationByIdCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductRestorationByIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.ProductEventPublicationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductSoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkRestorationByIdsPort;
import vn.edu.uit.msshop.product.product.application.service.command.support.ProductVersionGuard;
import vn.edu.uit.msshop.product.product.domain.event.ProductRestoredEvent;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

@Service
@RequiredArgsConstructor
class ProductRestorationByIdService
        implements ProductRestorationByIdUseCase {
    private final ProductSoftDeletedLookupByIdPort softDeletedLookupByIdPort;
    private final ProductUpdatePort updatePort;
    private final ProductVariantBulkRestorationByIdsPort variantBulkRestorationForProductPort;

    private final ProductEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.PRODUCT_LIST,
            allEntries = true)
    public void restore(
            final ProductRestorationByIdCommand cmd) {
        final var productId = new ProductId(cmd.productId());
        final var expectedVersion = new ProductVersion(cmd.productVersion());

        final var product = this.softDeletedLookupByIdPort
                .loadSoftDeletedById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductVersionGuard.ensureMatch(
                expectedVersion,
                product.getVersion());

        final var next = product.restored();
        final var saved = this.updatePort.update(next);

        final var variantIdSet = saved.getVariants().getValues().stream()
                .map(ProductVariant::id)
                .collect(Collectors.toUnmodifiableSet());
        this.variantBulkRestorationForProductPort.restoreByVariantIds(
                variantIdSet,
                productId);

        this.eventPublicationPort.publishEvent(new ProductRestoredEvent(saved.getId()));
    }
}
