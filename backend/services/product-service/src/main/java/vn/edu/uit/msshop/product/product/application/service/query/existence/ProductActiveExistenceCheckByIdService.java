package vn.edu.uit.msshop.product.product.application.service.query.existence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByIdQuery;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductActiveExistenceCheckByIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
public class ProductActiveExistenceCheckByIdService
        implements ProductActiveExistenceCheckByIdUseCase {

    private final ProductExistenceCheckByIdPort existenceCheckPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean exists(
            final ProductActiveExistenceCheckByIdQuery query) {
        final var productId = new ProductId(query.productId());
        return this.existenceCheckPort.existsById(productId);
    }
}
