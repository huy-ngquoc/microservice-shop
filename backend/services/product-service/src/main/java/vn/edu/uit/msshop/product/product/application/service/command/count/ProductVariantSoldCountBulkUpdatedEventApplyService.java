package vn.edu.uit.msshop.product.product.application.service.command.count;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductSoldCountDecreaseForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductSoldCountIncreaseForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductVariantSoldCountBulkUpdatedEventApplyCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductVariantSoldCountBulkUpdatedEventApplyCommand.ProductVariantSoldCountDelta;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductSoldCountDecrementForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductSoldCountIncrementForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductVariantSoldCountBulkUpdatedEventApplyUseCase;

@Service
@RequiredArgsConstructor
class ProductVariantSoldCountBulkUpdatedEventApplyService
        implements ProductVariantSoldCountBulkUpdatedEventApplyUseCase {

    private final ProductSoldCountIncrementForVariantsUseCase incrementUseCase;
    private final ProductSoldCountDecrementForVariantsUseCase decrementUseCase;

    @Override
    @Transactional(
            propagation = Propagation.REQUIRES_NEW)
    public void apply(
            final ProductVariantSoldCountBulkUpdatedEventApplyCommand cmd) {
        final var deltas = ProductVariantSoldCountBulkUpdatedEventApplyService.toDeltasByProductId(cmd.deltas());
        if (!deltas.increments().isEmpty()) {
            final var newCommand = new ProductSoldCountIncreaseForVariantsCommand(deltas.increments());
            this.incrementUseCase.increase(newCommand);
        }
        if (!deltas.decrements().isEmpty()) {
            final var newCommand = new ProductSoldCountDecreaseForVariantsCommand(deltas.decrements());
            this.decrementUseCase.decrease(newCommand);
        }
    }

    private static DeltasByProductId toDeltasByProductId(
            final List<ProductVariantSoldCountDelta> deltaList) {
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
