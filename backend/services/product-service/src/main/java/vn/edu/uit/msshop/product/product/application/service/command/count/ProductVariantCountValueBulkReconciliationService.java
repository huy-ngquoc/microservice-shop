package vn.edu.uit.msshop.product.product.application.service.command.count;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductSoldCountValueReconciliationByProductIdCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductStockCountValueReconciliationByProductIdCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductSoldCountValueReconciliationByProductIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductStockCountValueReconciliationByProductIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductVariantCountValueBulkReconciliationUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.listing.ProductActiveListingPort;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;

@Service
@RequiredArgsConstructor
@Slf4j
class ProductVariantCountValueBulkReconciliationService
        implements ProductVariantCountValueBulkReconciliationUseCase {

    private static final int PAGE_SIZE = 100;

    private final ProductActiveListingPort activeListingPort;
    private final ProductSoldCountValueReconciliationByProductIdUseCase soldReconciliationUseCase;
    private final ProductStockCountValueReconciliationByProductIdUseCase stockReconciliationUseCase;

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
        final var soldCountValueReconciliation = new ProductSoldCountValueReconciliationByProductIdCommand(productId);
        final var stockCountValueReconciliation = new ProductStockCountValueReconciliationByProductIdCommand(productId);
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
