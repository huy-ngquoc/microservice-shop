package vn.edu.uit.msshop.product.product.application.port.in.command;

import org.springframework.lang.Nullable;

import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseSoldCountCommand;
import vn.edu.uit.msshop.product.product.domain.model.Product;

public interface IncreaseSoldCountUseCase {
    @Nullable
    public Product increaseSoldCountAmount(IncreaseSoldCountCommand command);
}
