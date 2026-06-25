package vn.edu.uit.msshop.product.product.application.service.command.variant;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantProjectionRebuildByProductIdCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantProjectionBulkRebuildUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantProjectionRebuildByProductIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.listing.ProductActiveListingPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;

@Service
@RequiredArgsConstructor
@Slf4j
class ProductVariantProjectionBulkRebuildService
        implements ProductVariantProjectionBulkRebuildUseCase {

    private static final int PAGE_SIZE = 100;

    private final ProductActiveListingPort activeListingPort;
    private final ProductVariantProjectionRebuildByProductIdUseCase perProductRebuildUseCase;

    @Override
    public void rebuildAll() {
        int page = 0;
        while (true) {
            final var pageRequest = new PageRequestDto(page, PAGE_SIZE);
            final var response = this.activeListingPort.list(pageRequest);
            for (final var product : response.items()) {
                this.rebuildOne(product.getId());
            }

            if (!response.hasNext()) {
                break;
            }
            page++;
        }
    }

    private void rebuildOne(
            final ProductId productId) {
        try {
            final var cmd = new ProductVariantProjectionRebuildByProductIdCommand(productId.value());
            this.perProductRebuildUseCase.rebuild(cmd);
        } catch (final RuntimeException e) {
            log.error("Product-variant projection rebuild failed for product {}", productId.value(), e);
        }
    }
}
