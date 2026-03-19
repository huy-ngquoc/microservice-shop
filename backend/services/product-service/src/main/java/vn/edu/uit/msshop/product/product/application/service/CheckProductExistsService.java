package vn.edu.uit.msshop.product.product.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.in.CheckProductExistsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.CheckProductExistsPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;

@Service
@RequiredArgsConstructor
public class CheckProductExistsService implements CheckProductExistsUseCase {
    private final CheckProductExistsPort checkExistsPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsById(
            final ProductId id) {
        return this.checkExistsPort.existsById(id);
    }
}
