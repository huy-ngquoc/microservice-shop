package vn.edu.uit.msshop.product.product.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.query.FindProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
public class FindProductService implements FindProductUseCase {
    private final LoadProductPort loadPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public ProductView findById(
            final ProductId id) {
        return this.loadPort.loadById(id)
                .map(this.mapper::toView)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
