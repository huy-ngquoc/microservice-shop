package vn.edu.uit.msshop.product.variant.application.service.command;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.port.in.command.ReconcileVariantSoldCountsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SetVariantSoldCountsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.FetchOrderSoldCountsPort;

@Service
@RequiredArgsConstructor
public class ReconcileVariantSoldCountsService
        implements ReconcileVariantSoldCountsUseCase {
    private final FetchOrderSoldCountsPort fetchOrderSoldCountsPort;
    private final SetVariantSoldCountsUseCase setVariantSoldCountsUseCase;

    @Override
    public void execute() {
        final var fetched = this.fetchOrderSoldCountsPort.fetchAll();
        if (fetched.isEmpty()) {
            return;
        }
        this.setVariantSoldCountsUseCase.execute(fetched);
    }
}
