package vn.edu.uit.msshop.product.category.adapter.out.persistence.mapper;

import org.jspecify.annotations.NullUnmarked;
import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.category.adapter.out.persistence.CategoryJpaEntity;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImage;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageSize;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageUrl;
import vn.edu.uit.msshop.product.category.domain.model.CategoryName;

@Component
public class CategoryEntityMapper {
    public Category toDomain(
            final CategoryJpaEntity entity) {
        final var id = new CategoryId(entity.getId());
        final var name = new CategoryName(entity.getName());
        final var image = CategoryEntityMapper.toImageOrNull(
                entity.getImageUrl(),
                entity.getImageKey(),
                entity.getImageWidth(),
                entity.getImageHeight());

        return new Category(
                id,
                name,
                image);
    }

    public CategoryJpaEntity toEntity(
            final Category category) {
        final var image = category.getImage();

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
                category.getId().value(),
                category.getName().value(),
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
