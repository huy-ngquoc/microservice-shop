package vn.edu.uit.msshop.product.brand.application.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.brand.application.dto.query.BrandLogoView;
import vn.edu.uit.msshop.product.brand.application.dto.query.BrandView;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoKey;

@Component
public class BrandViewMapper {
    public BrandView toView(
            final Brand brand) {
        return new BrandView(
                brand.getId().value(),
                brand.getName().value(),
                BrandLogoKey.unwrap(brand.getLogoKey()),
                brand.getVersion().value());
    }

    public BrandLogoView toLogoView(
            final Brand brand) {
        return new BrandLogoView(
                brand.getId().value(),
                BrandLogoKey.unwrap(brand.getLogoKey()),
                brand.getVersion().value());
    }
}
