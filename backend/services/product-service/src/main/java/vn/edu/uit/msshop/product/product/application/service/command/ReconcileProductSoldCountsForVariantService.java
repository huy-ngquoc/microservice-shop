package vn.edu.uit.msshop.product.product.application.service.command;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
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
        final var absoluteByProductId = command.items().stream()
                .collect(Collectors.toMap(
                        ReconcileProductSoldCountsForVariantCommand.Item::productId,
                        ReconcileProductSoldCountsForVariantCommand.Item::newSoldCount,
                        Integer::sum));

        final var loadedById = this.loadProducts(absoluteByProductId.keySet());

        final var updated = absoluteByProductId.entrySet().stream()
                .filter(e -> loadedById.containsKey(e.getKey()))
                .map(e -> ReconcileProductSoldCountsForVariantService
                        .withNewSoldCount(loadedById.get(e.getKey()), e.getValue()))
                .toList();

        this.updateAllPort.updateAll(updated);
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
