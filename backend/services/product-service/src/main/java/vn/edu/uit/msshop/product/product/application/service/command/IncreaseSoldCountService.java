package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseSoldCountCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.IncreaseSoldCountUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.SaveProductPort;
import vn.edu.uit.msshop.product.product.domain.model.Product;

@Service
@RequiredArgsConstructor
public class IncreaseSoldCountService implements IncreaseSoldCountUseCase {
    //private final LoadVariantPort loadPort;
    private final SaveProductPort savePort;
    private final LoadProductPort loadPort;
    @Override
    @Nullable
    public Product increaseSoldCountAmount(IncreaseSoldCountCommand command) {
        final var productOptional = loadPort.loadById(command.id());
        if(productOptional.isEmpty()) return null;
        return savePort.save(productOptional.get().increaseSoldCount(command.increaseAmount()));
        
        
    }

}
