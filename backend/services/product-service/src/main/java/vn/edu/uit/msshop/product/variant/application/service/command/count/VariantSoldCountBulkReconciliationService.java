package vn.edu.uit.msshop.product.variant.application.service.command.count;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.command.count.VariantSoldCountBulkSetCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.count.VariantSoldCountBulkReconciliationUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.count.VariantSoldCountBulkSetUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.FetchAllOrderSoldCountsPort;
import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantOrderSoldCount;

@Service
@RequiredArgsConstructor
class VariantSoldCountBulkReconciliationService
        implements VariantSoldCountBulkReconciliationUseCase {

    private final FetchAllOrderSoldCountsPort fetchAllPort;
    private final VariantSoldCountBulkSetUseCase setAllUseCase;

    @Override
    public void execute() {
        final var soldCountCollection = this.fetchAllPort.fetchAll();
        if (soldCountCollection.isEmpty()) {
            return;
        }

        final var soldCountById = VariantSoldCountBulkReconciliationService
                .toSoldCountById(soldCountCollection);
        final var setCommand = new VariantSoldCountBulkSetCommand(soldCountById);
        this.setAllUseCase.execute(setCommand);
    }

    private static Map<UUID, Integer> toSoldCountById(
            final Collection<VariantOrderSoldCount> soldCountCollection) {
        final var soldCountById = HashMap.<UUID, Integer>newHashMap(
                soldCountCollection.size());
        for (final var soldCount : soldCountCollection) {
            final var variantId = soldCount.variantId();
            final var soldCountValue = soldCount.value();

            soldCountById.put(
                    variantId.value(),
                    soldCountValue.value());
        }
        return soldCountById;
    }
}
