package vn.edu.uit.msshop.product.product.application.service.command;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductSoldCountsForVariantCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.IncreaseProductSoldCountsForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadAllProductsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateAllProductsPort;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
public class IncreaseProductSoldCountsForVariantService
        implements IncreaseProductSoldCountsForVariantUseCase {
    private final LoadAllProductsPort loadAllPort;
    private final UpdateAllProductsPort updateAllPort;

    @Override
    @Transactional
    public void execute(
            final IncreaseProductSoldCountsForVariantCommand command) {
        final var incrementByProductId = IncreaseProductSoldCountsForVariantService
                .aggregateByProductId(command);
        final var loadedById = this.loadProducts(incrementByProductId.keySet());

        final var updated = IncreaseProductSoldCountsForVariantService.applyIncrements(
                loadedById,
                incrementByProductId);
        this.updateAllPort.updateAll(updated);
    }

    private static Map<ProductId, Integer> aggregateByProductId(
            final IncreaseProductSoldCountsForVariantCommand command) {
        return command.items().stream()
                .collect(Collectors.toMap(
                        IncreaseProductSoldCountsForVariantCommand.Item::productId,
                        IncreaseProductSoldCountsForVariantCommand.Item::soldCountIncrement,
                        Integer::sum));
    }

    private static List<Product> applyIncrements(
            final Map<ProductId, Product> loadedById,
            final Map<ProductId, Integer> incrementByProductId) {
        return incrementByProductId.entrySet().stream()
                .map(e -> IncreaseProductSoldCountsForVariantService.applyIncrement(
                        loadedById,
                        e.getKey(),
                        e.getValue()))
                .toList();
    }

    private static Product applyIncrement(
            final Map<ProductId, Product> loadedById,
            final ProductId productId,
            final int increment) {
        final var product = loadedById.get(productId);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        }
        return IncreaseProductSoldCountsForVariantService
                .withIncreasedSoldCount(product, increment);
    }

    private static Product withIncreasedSoldCount(
            final Product current,
            final int increment) {
        final var newSoldCount = current.getSoldCount().increase(increment);
        return new Product(
                current.getId(),
                current.getName(),
                current.getCategoryId(),
                current.getBrandId(),
                current.getPriceRange(),
                newSoldCount,
                current.getRating(),
                current.getConfiguration(),
                current.getImageKeys(),
                current.getVersion(),
                current.getDeletionTime());
    }

    private Map<ProductId, Product> loadProducts(
            final Collection<ProductId> ids) {
        return this.loadAllPort.loadAllByIds(ids).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }
}
