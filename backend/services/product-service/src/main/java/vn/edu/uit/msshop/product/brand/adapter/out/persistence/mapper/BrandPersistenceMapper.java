package vn.edu.uit.msshop.product.brand.adapter.out.persistence.mapper;

import org.jspecify.annotations.NullUnmarked;
import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.brand.adapter.out.persistence.BrandDocument;
import vn.edu.uit.msshop.product.brand.adapter.out.persistence.BrandLogoDocument;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogo;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoKey;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoSize;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoUrl;
import vn.edu.uit.msshop.product.brand.domain.model.BrandName;

@Component
public class BrandPersistenceMapper {
    public Brand toDomain(
            final BrandDocument entity) {
        final var id = new BrandId(entity.getId());
        final var name = new BrandName(entity.getName());

        final var logoDoc = entity.getLogo();
        final BrandLogo logo;
        if (logoDoc != null) {
            logo = BrandPersistenceMapper.toLogoOrNull(
                    logoDoc.getUrl(),
                    logoDoc.getKey(),
                    logoDoc.getWidth(),
                    logoDoc.getHeight());
        } else {
            logo = null;
        }

        return new Brand(id, name, logo);
    }

    public BrandDocument toPersistence(
            final Brand brand) {
        final var logo = brand.getLogo();

        final BrandLogoDocument logoDoc;
        if (logo != null) {
            logoDoc = new BrandLogoDocument(
                    logo.url().value(),
                    logo.key().value(),
                    logo.size().width(),
                    logo.size().height());
        } else {
            logoDoc = null;
        }

        return new BrandDocument(
                brand.getId().value(),
                brand.getName().value(),
                logoDoc);
    }

    @NullUnmarked
    private static BrandLogo toLogoOrNull(
            String url,
            String key,
            Integer width,
            Integer height) {
        if ((url == null) || (key == null) || (width == null) || (height == null)) {
            return null;
        }

        return BrandPersistenceMapper.toLogo(url, key, width, height);
    }

    private static BrandLogo toLogo(
            String url,
            String key,
            int width,
            int height) {
        final var imageSize = new BrandLogoSize(width, height);

        return new BrandLogo(
                new BrandLogoUrl(url),
                new BrandLogoKey(key),
                imageSize);
    }
}
