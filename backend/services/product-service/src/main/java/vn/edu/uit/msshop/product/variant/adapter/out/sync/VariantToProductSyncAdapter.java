package vn.edu.uit.msshop.product.variant.adapter.out.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductSoldCountDecreaseForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductStockCountDecreaseForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductStockCountIncreaseForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductSoldCountIncreaseForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantAdditionForVariantCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantRemovalForVariantCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantUpdateForVariantCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductSoldCountDecreaseForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductStockCountDecreaseForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductSoldCountIncreaseForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductStockCountIncreaseForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantAdditionForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantRemovalForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantUpdateForVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.AddVariantToProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.DecreaseProductSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.DecreaseProductStockCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.IncreaseProductSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.IncreaseProductStockCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.RemoveVariantFromProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.UpdateVariantInProductPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Component
@RequiredArgsConstructor
public class VariantToProductSyncAdapter
        implements
        AddVariantToProductPort,
        UpdateVariantInProductPort,
        IncreaseProductSoldCountsPort,
        IncreaseProductStockCountsPort,
        DecreaseProductSoldCountsPort,
        DecreaseProductStockCountsPort,
        RemoveVariantFromProductPort {

    private final ProductVariantAdditionForVariantUseCase productVariantAdditionForVariantUseCase;
    private final ProductVariantUpdateForVariantUseCase productVariantUpdateForVariantUseCase;
    private final ProductSoldCountIncreaseForVariantsUseCase productSoldCountIncreaseForVariantsUseCase;
    private final ProductStockCountIncreaseForVariantsUseCase productStockCountIncreaseForVariantsUseCase;
    private final ProductSoldCountDecreaseForVariantsUseCase productSoldCountDecreaseForVariantsUseCase;
    private final ProductStockCountDecreaseForVariantsUseCase productStockCountDecreaseForVariantsUseCase;
    private final ProductVariantRemovalForVariantUseCase productVariantRemovalForVariantUseCase;

    @Override
    public void addToProduct(
            final Variant variant,
            int soldIncrement,
            int stockIncrement) {
        final var command = new ProductVariantAdditionForVariantCommand(
                variant.getProductId().value(),
                variant.getId().value(),
                variant.getPrice().value(),
                variant.getTraits().unwrap(),
                soldIncrement,
                stockIncrement);
        this.productVariantAdditionForVariantUseCase.add(command);
    }

    @Override
    public void updateInProduct(
            final Variant variant) {
        final var command = new ProductVariantUpdateForVariantCommand(
                variant.getProductId().value(),
                variant.getId().value(),
                variant.getPrice().value(),
                variant.getTraits().unwrap());

        this.productVariantUpdateForVariantUseCase.update(command);
    }

    @Override
    public void increaseAllSoldCounts(
            final Map<VariantProductId, Integer> incrementByProductId) {
        final var incrementById = VariantToProductSyncAdapter
                .toCountByProductId(incrementByProductId);

        final var command = new ProductSoldCountIncreaseForVariantsCommand(incrementById);
        this.productSoldCountIncreaseForVariantsUseCase.increase(command);
    }

    @Override
    public void increaseAllStockCounts(
            final Map<VariantProductId, Integer> incrementByProductId) {
        final var incrementById = VariantToProductSyncAdapter
                .toCountByProductId(incrementByProductId);

        final var command = new ProductStockCountIncreaseForVariantsCommand(incrementById);
        this.productStockCountIncreaseForVariantsUseCase.increase(command);
    }

    @Override
    public void decreaseAllSoldCounts(
            Map<VariantProductId, Integer> decrementByProductId) {
        final var decrementById = VariantToProductSyncAdapter
                .toCountByProductId(decrementByProductId);

        final var command = new ProductSoldCountDecreaseForVariantsCommand(decrementById);
        this.productSoldCountDecreaseForVariantsUseCase.decrease(command);
    }

    @Override
    public void decreaseAllStockCounts(
            Map<VariantProductId, Integer> decrementByProductId) {
        final var decrementById = VariantToProductSyncAdapter
                .toCountByProductId(decrementByProductId);

        final var command = new ProductStockCountDecreaseForVariantsCommand(decrementById);
        this.productStockCountDecreaseForVariantsUseCase.decrease(command);
    }

    @Override
    public void removeFromProduct(
            final VariantId variantId,
            final VariantProductId variantProductId,
            int soldDecrement,
            int stockDecrement) {
        final var command = new ProductVariantRemovalForVariantCommand(
                variantProductId.value(),
                variantId.value(),
                soldDecrement,
                stockDecrement);

        this.productVariantRemovalForVariantUseCase.remove(command);
    }

    private static Map<UUID, Integer> toCountByProductId(
            final Map<VariantProductId, Integer> countByVariantProductId) {
        final var countByProductId = HashMap.<UUID, Integer>newHashMap(
                countByVariantProductId.size());
        for (final var entry : countByVariantProductId.entrySet()) {
            final var id = entry.getKey();
            final var idValue = id.value();
            final var count = entry.getValue();

            countByProductId.put(idValue, count);
        }
        return countByProductId;
    }
}
