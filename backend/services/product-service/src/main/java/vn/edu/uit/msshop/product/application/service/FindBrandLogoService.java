package vn.edu.uit.msshop.product.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.application.dto.query.BrandLogoView;
import vn.edu.uit.msshop.product.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.application.port.in.FindBrandLogoUseCase;
import vn.edu.uit.msshop.product.application.port.out.LoadBrandPort;
import vn.edu.uit.msshop.product.domain.model.brand.Brand;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;

@Service
@RequiredArgsConstructor
public class FindBrandLogoService implements FindBrandLogoUseCase {
    private final LoadBrandPort loadPort;
    private final BrandViewMapper mapper;

    public BrandLogoView findById(
            final BrandId id) {
        return this.loadPort.loadById(id)
                .map(Brand::getLogo)
                .map(this.mapper::toView)
                .orElseThrow(() -> new BrandNotFoundException(id));
    }
}
