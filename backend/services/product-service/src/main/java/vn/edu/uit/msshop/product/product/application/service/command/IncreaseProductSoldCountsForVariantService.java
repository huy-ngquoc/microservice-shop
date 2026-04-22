package vn.edu.uit.msshop.product.product.application.service.command;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductSoldCountsForVariantCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.IncreaseProductSoldCountsForVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.IncreaseProductSoldCountsPort;

@Service
@RequiredArgsConstructor
public class IncreaseProductSoldCountsForVariantService
        implements IncreaseProductSoldCountsForVariantUseCase {
    private final IncreaseProductSoldCountsPort increaseSoldCountsPort;

    @Override
    public void execute(
            final IncreaseProductSoldCountsForVariantCommand command) {
        final var incrementByProductId = command.items().stream()
                .collect(Collectors.toMap(
                        IncreaseProductSoldCountsForVariantCommand.Item::productId,
                        IncreaseProductSoldCountsForVariantCommand.Item::soldCountIncrement,
                        Integer::sum));
        this.increaseSoldCountsPort.increaseAll(incrementByProductId);
    }
}
