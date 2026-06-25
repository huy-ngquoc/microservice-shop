package vn.edu.uit.msshop.product.product.application.service.command.count;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductStockCountDecreaseForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductStockCountIncreaseForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductVariantStockCountBulkUpdatedEventApplyCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductVariantStockCountBulkUpdatedEventApplyCommand.ProductVariantStockCountDelta;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductStockCountDecrementForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductStockCountIncrementForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductVariantStockCountBulkUpdatedEventApplyUseCase;

@Service
@RequiredArgsConstructor
class ProductVariantStockCountBulkUpdatedEventApplyService
        implements ProductVariantStockCountBulkUpdatedEventApplyUseCase {

    private final ProductStockCountIncrementForVariantsUseCase incrementUseCase;
    private final ProductStockCountDecrementForVariantsUseCase decrementUseCase;

    @Override
    @Transactional(
            propagation = Propagation.REQUIRES_NEW)
    public void apply(
            final ProductVariantStockCountBulkUpdatedEventApplyCommand cmd) {
        final var deltas = ProductVariantStockCountBulkUpdatedEventApplyService.toDeltasByProductId(cmd.deltas());
        if (!deltas.increments().isEmpty()) {
            final var newCommand = new ProductStockCountIncreaseForVariantsCommand(deltas.increments());
            this.incrementUseCase.increase(newCommand);
        }
        if (!deltas.decrements().isEmpty()) {
            final var newCommand = new ProductStockCountDecreaseForVariantsCommand(deltas.decrements());
            this.decrementUseCase.decrease(newCommand);
        }
    }

    private static DeltasByProductId toDeltasByProductId(
            final List<ProductVariantStockCountDelta> deltaList) {
        final var increments = new HashMap<UUID, Integer>();
        final var decrements = new HashMap<UUID, Integer>();
        for (final var delta : deltaList) {
            if (delta.delta() > 0) {
                increments.merge(delta.productId(), delta.delta(), Integer::sum);
            } else if (delta.delta() < 0) {
                decrements.merge(delta.productId(), -delta.delta(), Integer::sum);
            } else {
                // Do nothing
            }
        }
        return new DeltasByProductId(increments, decrements);
    }

    private record DeltasByProductId(
            Map<UUID, Integer> increments,
            Map<UUID, Integer> decrements) {

        private DeltasByProductId {
            increments = Map.copyOf(increments);
            decrements = Map.copyOf(decrements);
        }
    }
}
