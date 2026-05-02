package vn.edu.uit.msshop.product.variant.application.service.command;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.port.in.command.ReconcileVariantSoldCountsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SetAllVariantSoldCountsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.FetchAllOrderSoldCountsPort;

@Service
@RequiredArgsConstructor
public class ReconcileVariantSoldCountsService
        implements ReconcileVariantSoldCountsUseCase {
    private final FetchAllOrderSoldCountsPort fetchAllPort;
    private final SetAllVariantSoldCountsUseCase setAllUseCase;

    @Override
    public void execute() {
        final var fetched = this.fetchAllPort.fetchAll();
        if (fetched.isEmpty()) {
            return;
        }
        this.setAllUseCase.execute(fetched);
    }
}
