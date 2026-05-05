package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.RestoreBrandCommand;

public interface RestoreBrandUseCase {
  void restore(final RestoreBrandCommand command);
}
