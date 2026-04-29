package vn.edu.uit.msshop.product.product.application.service.command;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductSoldCountsForVariantCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.IncreaseProductSoldCountsForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.IncreaseAllProductSoldCountsPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
public class IncreaseProductSoldCountsForVariantService
        implements IncreaseProductSoldCountsForVariantUseCase {
    private static final Collector<
            IncreaseProductSoldCountsForVariantCommand.Item,
            ?,
            Map<ProductId, Integer>> SUM_INCREMENTS_BY_PRODUCT_ID = Collectors
                    .groupingBy(
                            IncreaseProductSoldCountsForVariantCommand.Item::productId,
                            Collectors.summingInt(
                                    IncreaseProductSoldCountsForVariantCommand.Item::soldCountIncrement));

    private final IncreaseAllProductSoldCountsPort increaseAllSoldCountsPort;

    @Override
    public void execute(
            final IncreaseProductSoldCountsForVariantCommand command) {
        final var incrementByProductId = command.items().stream()
                .collect(IncreaseProductSoldCountsForVariantService.SUM_INCREMENTS_BY_PRODUCT_ID);
        this.increaseAllSoldCountsPort.increaseAll(incrementByProductId);
    }
}
