package vn.edu.uit.msshop.product.product.application.service.command.count;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductSoldCountReconciliationByProductIdCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductStockCountReconciliationByProductIdCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductSoldCountReconciliationByProductIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductStockCountReconciliationByProductIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductVariantCountBulkReconciliationUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.listing.ProductActiveListingPort;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;

@Service
@RequiredArgsConstructor
@Slf4j
class ProductVariantCountBulkReconciliationService
        implements ProductVariantCountBulkReconciliationUseCase {

    private static final int PAGE_SIZE = 100;

    private final ProductActiveListingPort activeListingPort;
    private final ProductSoldCountReconciliationByProductIdUseCase soldReconciliationUseCase;
    private final ProductStockCountReconciliationByProductIdUseCase stockReconciliationUseCase;

    @Override
    public void reconcileAll() {
        int page = 0;
        while (true) {
            final var pageRequest = new PageRequestDto(page, PAGE_SIZE);
            final var response = this.activeListingPort.list(pageRequest);
            for (final var product : response.items()) {
                this.reconcileOne(product.getId().value());
            }
            if (!response.hasNext()) {
                break;
            }
            page++;
        }
    }

    private void reconcileOne(
            final UUID productId) {
        final var soldCountValueReconciliation = new ProductSoldCountReconciliationByProductIdCommand(productId);
        final var stockCountValueReconciliation = new ProductStockCountReconciliationByProductIdCommand(productId);
        try {
            this.soldReconciliationUseCase.reconcile(soldCountValueReconciliation);
            this.stockReconciliationUseCase.reconcile(stockCountValueReconciliation);
        } catch (final RuntimeException exception) {
            log.error("Product count reconciliation failed for product {}",
                    productId,
                    exception);
        }
    }
}
