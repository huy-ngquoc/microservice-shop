package vn.edu.uit.msshop.product.product.application.service.command;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.ReconcileProductSoldCountsForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadAllProductsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateAllProductsPort;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductSoldCount;
import vn.edu.uit.msshop.product.variant.application.dto.command.ReconcileProductSoldCountsForVariantCommand;

@Service
@RequiredArgsConstructor
public class ReconcileProductSoldCountsForVariantService
        implements ReconcileProductSoldCountsForVariantUseCase {
    private final LoadAllProductsPort loadAllPort;
    private final UpdateAllProductsPort updateAllPort;

    @Override
    @Transactional
    public void execute(
            final ReconcileProductSoldCountsForVariantCommand command) {
        final var newSoldCountByProductId = ReconcileProductSoldCountsForVariantService
                .toNewSoldCountByProductIdFromCommand(command);

        final var loadedById = this.loadProducts(newSoldCountByProductId.keySet());

        final var updated = ReconcileProductSoldCountsForVariantService
                .applyNewCounts(loadedById, newSoldCountByProductId);

        this.updateAllPort.updateAll(updated);
    }

    private static Map<ProductId, Integer> toNewSoldCountByProductIdFromCommand(
            final ReconcileProductSoldCountsForVariantCommand command) {
        return command.items().stream()
                .collect(Collectors.toMap(
                        ReconcileProductSoldCountsForVariantCommand.Item::productId,
                        ReconcileProductSoldCountsForVariantCommand.Item::newSoldCount,
                        Integer::sum));
    }

    private static List<Product> applyNewCounts(
            final Map<ProductId, Product> loadedById,
            final Map<ProductId, Integer> newSoldCountByProductId) {
        return newSoldCountByProductId.entrySet().stream()
                .filter(e -> loadedById.containsKey(e.getKey()))
                .map(e -> ReconcileProductSoldCountsForVariantService
                        .applyNewCount(loadedById, e.getKey(), e.getValue()))
                .toList();
    }

    private static Product applyNewCount(
            final Map<ProductId, Product> loadedById,
            final ProductId productId,
            final int newSoldCount) {
        final var product = loadedById.get(productId);

        if (product == null) {
            throw new ProductNotFoundException(productId);
        }

        return ReconcileProductSoldCountsForVariantService
                .withNewSoldCount(product, newSoldCount);
    }

    private static Product withNewSoldCount(
            final Product current,
            final int newSoldCount) {
        return new Product(
                current.getId(),
                current.getName(),
                current.getCategoryId(),
                current.getBrandId(),
                current.getPriceRange(),
                new ProductSoldCount(newSoldCount),
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
