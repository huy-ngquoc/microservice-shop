package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductSoldCountsForVariantsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.IncreaseProductSoldCountsForVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.IncreaseAllProductSoldCountsPort;

@Service
@RequiredArgsConstructor
public class IncreaseProductSoldCountsForVariantsService
    implements IncreaseProductSoldCountsForVariantsUseCase {
  private final IncreaseAllProductSoldCountsPort increaseAllSoldCountsPort;

  @Override
  public void execute(final IncreaseProductSoldCountsForVariantsCommand command) {
    this.increaseAllSoldCountsPort.increaseAll(command.incrementById());
  }
}
