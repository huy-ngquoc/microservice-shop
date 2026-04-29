package vn.edu.uit.msshop.product.variant.adapter.out.sync;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.AddProductVariantForVariantCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductSoldCountsForVariantCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductStockCountsForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.RemoveProductVariantForVariantCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductVariantForVariantCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.AddProductVariantForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.IncreaseProductSoldCountsForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.IncreaseProductStockCountsForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.RemoveProductVariantForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.UpdateProductVariantForVariantUseCase;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.AddVariantToProductPort;
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
        RemoveVariantFromProductPort {
    private static final Collector<
            Map.Entry<VariantProductId, Integer>,
            ?,
            Map<ProductId, Integer>> INCREMENT_BY_PRODUCT_ID_COLLECTOR = Collectors
                    .toUnmodifiableMap(
                            entry -> new ProductId(entry.getKey().value()),
                            Map.Entry::getValue);

    private final AddProductVariantForVariantUseCase addProductVariantForVariantUseCase;
    private final UpdateProductVariantForVariantUseCase updateProductVariantForVariantUseCase;
    private final IncreaseProductSoldCountsForVariantUseCase increaseProductSoldCountsForVariantUseCase;
    private final IncreaseProductStockCountsForVariantsUseCase increaseProductStockCountsForVariantsUseCase;
    private final RemoveProductVariantForVariantUseCase removeProductVariantForVariantUseCase;

    @Override
    public void addToProduct(
            final Variant variant) {
        final var productId = new ProductId(variant.getProductId().value());
        final var productVariant = VariantToProductSyncAdapter.toProductVariant(variant);

        final var command = new AddProductVariantForVariantCommand(
                productId,
                productVariant);

        this.addProductVariantForVariantUseCase.addVariant(command);
    }

    @Override
    public void updateInProduct(
            final Variant variant) {
        final var productId = new ProductId(variant.getProductId().value());
        final var productVariant = VariantToProductSyncAdapter.toProductVariant(variant);

        final var command = new UpdateProductVariantForVariantCommand(
                productId,
                productVariant);

        this.updateProductVariantForVariantUseCase.updateVariant(command);
    }

    @Override
    public void removeFromProduct(
            final VariantId variantId,
            final VariantProductId variantProductId) {
        final var productVariantId = new ProductVariantId(variantId.value());
        final var productId = new ProductId(variantProductId.value());

        final var command = new RemoveProductVariantForVariantCommand(
                productId,
                productVariantId);

        this.removeProductVariantForVariantUseCase.removeVariant(command);
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
                .collect(VariantToProductSyncAdapter.INCREMENT_BY_PRODUCT_ID_COLLECTOR);

        final var command = new IncreaseProductSoldCountsForVariantCommand(incrementById);
        this.increaseProductSoldCountsForVariantUseCase.execute(command);
    }

    @Override
    public void increaseAllStockCounts(
            final Map<VariantProductId, Integer> incrementByProductId) {
        final var incrementById = incrementByProductId.entrySet().stream()
                .collect(VariantToProductSyncAdapter.INCREMENT_BY_PRODUCT_ID_COLLECTOR);

        final var command = new IncreaseProductStockCountsForVariantsCommand(incrementById);
        this.increaseProductStockCountsForVariantsUseCase.execute(command);
    }
}
