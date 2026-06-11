package vn.edu.uit.msshop.product.variant.application.service.command.count;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.command.SetAllVariantSoldCountsCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.ReconcileVariantSoldCountsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SetAllVariantSoldCountsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.FetchAllOrderSoldCountsPort;

@Service
@RequiredArgsConstructor
class VariantSoldCountBulkReconciliationService
        implements ReconcileVariantSoldCountsUseCase {

    private final FetchAllOrderSoldCountsPort fetchAllPort;
    private final SetAllVariantSoldCountsUseCase setAllUseCase;

    @Override
    public void execute() {
        final var fetched = this.fetchAllPort.fetchAll();
        if (fetched.isEmpty()) {
            return;
        }

        final var command = new SetAllVariantSoldCountsCommand(fetched);
        this.setAllUseCase.execute(command);
    }
}
