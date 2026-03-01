package vn.edu.uit.msshop.product.adapter.out.persistence.mapper;

import org.jspecify.annotations.NullUnmarked;
import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.adapter.out.persistence.BrandJpaEntity;
import vn.edu.uit.msshop.product.domain.model.brand.Brand;
import vn.edu.uit.msshop.product.domain.model.brand.snapshot.BrandSnapshot;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogo;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogoKey;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogoSize;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogoUrl;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandName;

@Component
public class BrandEntityMapper {
    public Brand toDomain(
            final BrandJpaEntity entity) {
        final var logo = BrandEntityMapper.toLogoOrNull(
                entity.getLogoUrl(),
                entity.getLogoKey(),
                entity.getLogoWidth(),
                entity.getLogoHeight());

        final var snapshot = new BrandSnapshot(
                new BrandId(entity.getId()),
                new BrandName(entity.getName()),
                logo);

        return Brand.reconstitute(snapshot);
    }

    public BrandJpaEntity toEntity(
            final Brand brand) {
        final var snapshot = brand.snapshot();

        final var logo = snapshot.logo();

        final String urlValue;
        final String keyValue;
        final Integer width;
        final Integer height;
        if (logo != null) {
            urlValue = logo.url().value();
            keyValue = logo.key().value();
            width = logo.size().width();
            height = logo.size().height();
        } else {
            urlValue = null;
            keyValue = null;
            width = null;
            height = null;
        }

        return BrandJpaEntity.of(
                snapshot.id().value(),
                snapshot.name().value(),
                urlValue,
                keyValue,
                width,
                height);
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

        return BrandEntityMapper.toLogo(url, key, width, height);
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
