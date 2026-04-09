package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseSoldCountCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.IncreaseSoldCountUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.SaveProductPort;
import vn.edu.uit.msshop.product.product.domain.model.Product;

@Service
@RequiredArgsConstructor
public class IncreaseSoldCountService implements IncreaseSoldCountUseCase {
    private final SaveProductPort savePort;
    private final LoadProductPort loadPort;

    @Override
    public Product increaseSoldCountAmount(
            IncreaseSoldCountCommand command) {
        final var productId = command.id();
        final var product = loadPort.loadById(command.id())
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return savePort.save(product.increaseSoldCount(command.increaseAmount()));
    }
}
