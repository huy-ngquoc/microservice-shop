package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductSoldCountsForVariantCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.IncreaseProductSoldCountsForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.IncreaseAllProductSoldCountsPort;

@Service
@RequiredArgsConstructor
public class IncreaseProductSoldCountsForVariantService
        implements IncreaseProductSoldCountsForVariantUseCase {
    private final IncreaseAllProductSoldCountsPort increaseAllSoldCountsPort;

    @Override
    public void execute(
            final IncreaseProductSoldCountsForVariantCommand command) {
        this.increaseAllSoldCountsPort.increaseAll(command.incrementById());
    }
}
