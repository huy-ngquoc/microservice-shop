package vn.edu.uit.msshop.product.product.application.service.command.variant;

import java.util.Set;

import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantProjectionRebuildByProductIdCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantProjectionRebuildByProductIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkFetchByProductIdPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
class ProductVariantProjectionRebuildByProductIdService
        implements ProductVariantProjectionRebuildByProductIdUseCase {

    private final ProductActiveLookupByIdPort activeLookupByIdPort;
    private final ProductVariantBulkFetchByProductIdPort variantBulkFetchByProductIdPort;

    private final ProductVariantProjectionPersister projectionPersister;

    @Override
    @Transactional
    @Retryable(
            includes = OptimisticLockException.class,
            maxRetries = 3,
            delay = 50,
            multiplier = 2.0,
            maxDelay = 500)
    public void rebuild(
            final ProductVariantProjectionRebuildByProductIdCommand cmd) {
        final var productId = new ProductId(cmd.productId());

        final var product = this.activeLookupByIdPort.loadById(productId).orElse(null);
        if (product == null) {
            return;
        }

        final var variantList = this.variantBulkFetchByProductIdPort.fetchAllActiveByProductId(productId);
        final var variants = new ProductVariants(variantList);
        if (variants.isEmpty()) {
            return;
        }

        final var current = product.getVariants();
        final var currentVariantSet = Set.copyOf(current.values());
        final var newVariantSet = Set.copyOf(variants.values());
        if (currentVariantSet.equals(newVariantSet)) {
            return;
        }

        final var newConfig = new ProductConfiguration(
                product.getOptions(),
                variants);
        final var next = product.changeConfiguration(newConfig);
        this.projectionPersister.persist(next);
    }
}
