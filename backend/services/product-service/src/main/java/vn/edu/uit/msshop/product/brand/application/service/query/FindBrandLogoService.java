package vn.edu.uit.msshop.product.brand.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.dto.query.BrandLogoView;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.query.FindBrandLogoUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

@Service
@RequiredArgsConstructor
public class FindBrandLogoService implements FindBrandLogoUseCase {
    private final LoadBrandPort loadPort;
    private final BrandViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public BrandLogoView findLogoById(
            final BrandId id) {
        return this.loadPort.loadById(id)
                .map(this.mapper::toLogoView)
                .orElseThrow(() -> new BrandNotFoundException(id));
    }
}
