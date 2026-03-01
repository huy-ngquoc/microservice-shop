package vn.edu.uit.msshop.product.application.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.application.dto.query.BrandLogoView;
import vn.edu.uit.msshop.product.application.dto.query.BrandView;
import vn.edu.uit.msshop.product.domain.model.brand.Brand;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogo;

@Component
public class BrandViewMapper {
    public BrandView toView(
            final Brand brand) {
        final var logo = brand.getLogo();

        String logoUrlValue = null;
        if (logo != null) {
            logoUrlValue = logo.url().value();
        }

        return new BrandView(
                brand.getId().value(),
                brand.getName().value(),
                logoUrlValue);
    }

    public BrandLogoView toView(
            final BrandLogo logo) {
        return new BrandLogoView(
                logo.url().value(),
                logo.size().width(),
                logo.size().height());
    }
}
