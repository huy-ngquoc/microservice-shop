package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductStockCountsForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.IncreaseProductStockCountsForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.IncreaseAllProductStockCountsPort;

@Service
@RequiredArgsConstructor
public class IncreaseProductStockCountsForVariantsService
    implements IncreaseProductStockCountsForVariantsUseCase {
  private final IncreaseAllProductStockCountsPort increaseAllPort;

  @Override
  public void execute(final IncreaseProductStockCountsForVariantsCommand command) {
    this.increaseAllPort.increaseAll(command.incrementById());
  }
}
