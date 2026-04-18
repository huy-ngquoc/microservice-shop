package vn.edu.uit.msshop.product.variant.application.service.command;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.port.in.command.ReconcileVariantSoldCountsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.FetchOrderSoldCountsPort;

@Service
@RequiredArgsConstructor
public class ReconcileVariantSoldCountsService
        implements ReconcileVariantSoldCountsUseCase {
    private final FetchOrderSoldCountsPort fetchOrderSoldCountsPort;
    private final ReconcileVariantSoldCountsWriter writer;

    @Override
    public void execute() {
        final var orderSoldCounts = this.fetchOrderSoldCountsPort.fetchAll();
        if (orderSoldCounts.isEmpty()) {
            return;
        }

        this.writer.write(orderSoldCounts);
    }
}
