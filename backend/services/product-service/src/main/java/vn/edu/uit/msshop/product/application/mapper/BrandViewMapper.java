package vn.edu.uit.msshop.product.application.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.application.dto.query.BrandView;
import vn.edu.uit.msshop.product.domain.model.brand.Brand;

@Component
public class BrandViewMapper {
    public BrandView toView(
            final Brand brand) {
        return new BrandView(
                brand.getId().value(),
                brand.getName().value(),
                brand.getLogo().url().value());
    }
}
