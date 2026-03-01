package vn.edu.uit.msshop.product.adapter.out.persistence.mapper;

import org.jspecify.annotations.NullUnmarked;
import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.adapter.out.persistence.CategoryJpaEntity;
import vn.edu.uit.msshop.product.domain.model.category.Category;
import vn.edu.uit.msshop.product.domain.model.category.snapshot.CategorySnapshot;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImage;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImageKey;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImageSize;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImageUrl;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryName;

@Component
public class CategoryEntityMapper {
    public Category toDomain(
            final CategoryJpaEntity entity) {
        final var image = CategoryEntityMapper.toImageOrNull(
                entity.getImageUrl(),
                entity.getImageKey(),
                entity.getImageWidth(),
                entity.getImageHeight());

        final var snapshot = CategorySnapshot.builder()
                .id(new CategoryId(entity.getId()))
                .name(new CategoryName(entity.getName()))
                .image(image)
                .build();

        return Category.reconstitute(snapshot);
    }

    public CategoryJpaEntity toEntity(
            final Category category) {
        final var snapshot = category.snapshot();

        final var image = snapshot.image();

        final String urlValue;
        final String keyValue;
        final Integer width;
        final Integer height;
        if (image != null) {
            urlValue = image.url().value();
            keyValue = image.key().value();
            width = image.size().width();
            height = image.size().height();
        } else {
            urlValue = null;
            keyValue = null;
            width = null;
            height = null;
        }

        return CategoryJpaEntity.of(
                snapshot.id().value(),
                snapshot.name().value(),
                urlValue,
                keyValue,
                width,
                height);
    }

    @NullUnmarked
    private static CategoryImage toImageOrNull(
            String url,
            String key,
            Integer width,
            Integer height) {
        if ((url == null) || (key == null) || (width == null) || (height == null)) {
            return null;
        }

        return CategoryEntityMapper.toImage(url, key, width, height);
    }

    private static CategoryImage toImage(
            String url,
            String key,
            int width,
            int height) {
        final var imageSize = new CategoryImageSize(width, height);

        return new CategoryImage(
                new CategoryImageUrl(url),
                new CategoryImageKey(key),
                imageSize);
    }
}
