package vn.edu.uit.msshop.product.variant.application.service.command.count;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.command.ReconcileVariantStockCountsCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.SetAllVariantStockCountsCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.count.VariantStockCountBulkReconciliationUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.count.VariantStockCountBulkSetUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.FetchInventoryStockCountsPort;

@Service
@RequiredArgsConstructor
class VariantStockCountBulkReconciliationService
        implements VariantStockCountBulkReconciliationUseCase {

    private final FetchInventoryStockCountsPort fetchPort;
    private final VariantStockCountBulkSetUseCase setAllUseCase;

    @Override
    public void execute(
            final ReconcileVariantStockCountsCommand command) {
        final var fetched = fetchPort.fetchAll(
                command.rangeStartTime(),
                command.rangeEndTime());
        if (fetched.isEmpty()) {
            return;
        }

        final var setCommand = new SetAllVariantStockCountsCommand(fetched);
        this.setAllUseCase.execute(setCommand);
    }

}
