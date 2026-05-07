package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.DecreaseProductStockCountsForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.DecreaseProductStockCountsForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.DecreaseAllProductStockCountsPort;

@Service
@RequiredArgsConstructor
public class DecreaseProductStockCountsForVariantsService
        implements DecreaseProductStockCountsForVariantsUseCase {
    private final DecreaseAllProductStockCountsPort decreaseAllPort;

    @Override
    @Transactional
    public void execute(
            DecreaseProductStockCountsForVariantsCommand command) {
        this.decreaseAllPort.decreaseAll(command.decrementById());
    }
}
