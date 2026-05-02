package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.DecreaseProductSoldCountsForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.DecreaseProductSoldCountsForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.DecreaseAllProductSoldCountsPort;

@Service
@RequiredArgsConstructor
public class DecreaseProductSoldCountsForVariantsService
        implements DecreaseProductSoldCountsForVariantsUseCase {
    private final DecreaseAllProductSoldCountsPort decreaseAllPort;

    @Override
    public void execute(
            DecreaseProductSoldCountsForVariantsCommand command) {
        this.decreaseAllPort.decreaseAll(command.decrementById());
    }

}
