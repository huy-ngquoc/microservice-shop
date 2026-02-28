package vn.edu.uit.msshop.product.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.application.dto.query.BrandView;
import vn.edu.uit.msshop.product.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.application.port.in.FindBrandUseCase;
import vn.edu.uit.msshop.product.application.port.out.LoadBrandPort;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;

@Service
@RequiredArgsConstructor
public class FindBrandService implements FindBrandUseCase {
    private final LoadBrandPort loadPort;
    private final BrandViewMapper mapper;

    public BrandView findById(
            final BrandId id) {
        return this.loadPort.loadById(id)
                .map(this.mapper::toView)
                .orElseThrow(() -> new BrandNotFoundException(id));
    }
}
