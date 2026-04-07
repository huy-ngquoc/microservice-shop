package vn.edu.uit.msshop.product.product.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.query.FindSoftDeletedProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadSoftDeletedProductPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
public class FindSoftDeletedProductService
        implements FindSoftDeletedProductUseCase {
    private final LoadSoftDeletedProductPort loadSoftDeletedPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional
    public ProductView findSoftDeletedById(
            final ProductId id) {
        return this.loadSoftDeletedPort
                .loadSoftDeletedById(id)
                .map(this.mapper::toView)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

}
