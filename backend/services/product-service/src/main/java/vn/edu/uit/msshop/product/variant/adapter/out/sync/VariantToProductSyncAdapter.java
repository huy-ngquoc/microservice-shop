package vn.edu.uit.msshop.product.variant.adapter.out.sync;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductVariantForVariantCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.count.DecreaseProductSoldCountsForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.count.DecreaseProductStockCountsForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.count.IncreaseProductSoldCountsForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.count.IncreaseProductStockCountsForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.AddProductVariantForVariantCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.RemoveProductVariantForVariantCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductSoldCountDecreaseForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductStockCountDecreaseForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductSoldCountIncreaseForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductStockCountIncreaseForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantAdditionForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantRemovalForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantUpdateForVariantUseCase;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
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
    private static final Collector<
            Map.Entry<VariantProductId, Integer>,
            ?,
            Map<ProductId, Integer>> BY_PRODUCT_ID_COLLECTOR = Collectors.toUnmodifiableMap(
                    entry -> new ProductId(entry.getKey().value()),
                    Map.Entry::getValue);

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
        final var productId = new ProductId(variant.getProductId().value());
        final var productVariant = VariantToProductSyncAdapter.toProductVariant(variant);

        final var command = new AddProductVariantForVariantCommand(
                productId,
                productVariant,
                soldIncrement,
                stockIncrement);

        this.productVariantAdditionForVariantUseCase.add(command);
    }

    @Override
    public void updateInProduct(
            final Variant variant) {
        final var productId = new ProductId(variant.getProductId().value());
        final var productVariant = VariantToProductSyncAdapter.toProductVariant(variant);

        final var command = new UpdateProductVariantForVariantCommand(
                productId,
                productVariant);

        this.productVariantUpdateForVariantUseCase.update(command);
    }

    private static ProductVariant toProductVariant(
            final Variant variant) {
        final var productVariantId = new ProductVariantId(variant.getId().value());
        final var productVariantPrice = new ProductVariantPrice(variant.getPrice().value());
        final var productVariantTraits = ProductVariantTraits.of(variant.getTraits().unwrap());

        return new ProductVariant(
                productVariantId,
                productVariantPrice,
                productVariantTraits);
    }

    @Override
    public void increaseAllSoldCounts(
            final Map<VariantProductId, Integer> incrementByProductId) {
        final var incrementById = incrementByProductId.entrySet().stream()
                .collect(VariantToProductSyncAdapter.BY_PRODUCT_ID_COLLECTOR);

        final var command = new IncreaseProductSoldCountsForVariantsCommand(incrementById);
        this.productSoldCountIncreaseForVariantsUseCase.increase(command);
    }

    @Override
    public void increaseAllStockCounts(
            final Map<VariantProductId, Integer> incrementByProductId) {
        final var incrementById = incrementByProductId.entrySet().stream()
                .collect(VariantToProductSyncAdapter.BY_PRODUCT_ID_COLLECTOR);

        final var command = new IncreaseProductStockCountsForVariantsCommand(incrementById);
        this.productStockCountIncreaseForVariantsUseCase.increase(command);
    }

    @Override
    public void decreaseAllSoldCounts(
            Map<VariantProductId, Integer> decrementByProductId) {
        final var decrementById = decrementByProductId.entrySet().stream()
                .collect(VariantToProductSyncAdapter.BY_PRODUCT_ID_COLLECTOR);

        final var command = new DecreaseProductSoldCountsForVariantsCommand(decrementById);
        this.productSoldCountDecreaseForVariantsUseCase.decrease(command);
    }

    @Override
    public void decreaseAllStockCounts(
            Map<VariantProductId, Integer> decrementByProductId) {
        final var decrementById = decrementByProductId.entrySet().stream()
                .collect(VariantToProductSyncAdapter.BY_PRODUCT_ID_COLLECTOR);

        final var command = new DecreaseProductStockCountsForVariantsCommand(decrementById);
        this.productStockCountDecreaseForVariantsUseCase.decrease(command);
    }

    @Override
    public void removeFromProduct(
            final VariantId variantId,
            final VariantProductId variantProductId,
            int soldDecrement,
            int stockDecrement) {
        final var productVariantId = new ProductVariantId(variantId.value());
        final var productId = new ProductId(variantProductId.value());

        final var command = new RemoveProductVariantForVariantCommand(
                productId,
                productVariantId,
                soldDecrement,
                stockDecrement);

        this.productVariantRemovalForVariantUseCase.remove(command);
    }
}
