package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductSoldCountsForVariantsCommand;

public interface IncreaseProductSoldCountsForVariantsUseCase {
  void execute(final IncreaseProductSoldCountsForVariantsCommand command);
}
