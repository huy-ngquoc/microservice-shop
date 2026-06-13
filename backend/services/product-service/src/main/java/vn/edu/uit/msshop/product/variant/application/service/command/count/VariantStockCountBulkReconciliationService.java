package vn.edu.uit.msshop.product.variant.application.service.command.count;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.command.count.VariantStockCountBulkReconciliationCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.count.VariantStockCountBulkSetCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.count.VariantStockCountBulkReconciliationUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.count.VariantStockCountBulkSetUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.FetchInventoryStockCountsPort;
import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantInventoryStockCount;

@Service
@RequiredArgsConstructor
class VariantStockCountBulkReconciliationService
        implements VariantStockCountBulkReconciliationUseCase {

    private final FetchInventoryStockCountsPort fetchPort;
    private final VariantStockCountBulkSetUseCase setAllUseCase;

    @Override
    public void execute(
            final VariantStockCountBulkReconciliationCommand cmd) {
        final var stockCountCollection = fetchPort.fetchAll(
                cmd.rangeStartTime(),
                cmd.rangeEndTime());
        if (stockCountCollection.isEmpty()) {
            return;
        }

        final var stockCountById = VariantStockCountBulkReconciliationService
                .toSoldCountById(stockCountCollection);
        final var setCommand = new VariantStockCountBulkSetCommand(stockCountById);
        this.setAllUseCase.execute(setCommand);
    }

    private static Map<UUID, Integer> toSoldCountById(
            final Collection<VariantInventoryStockCount> stockCountCollection) {
        final var stockCountById = HashMap.<UUID, Integer>newHashMap(
                stockCountCollection.size());
        for (final var stockCount : stockCountCollection) {
            stockCountById.put(
                    stockCount.variantId().value(),
                    stockCount.value().value());
        }
        return stockCountById;
    }
}
