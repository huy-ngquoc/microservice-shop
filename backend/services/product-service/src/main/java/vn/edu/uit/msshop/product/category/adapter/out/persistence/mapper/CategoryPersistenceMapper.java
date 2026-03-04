package vn.edu.uit.msshop.product.category.adapter.out.persistence.mapper;

import org.jspecify.annotations.NullUnmarked;
import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.category.adapter.out.persistence.CategoryDocument;
import vn.edu.uit.msshop.product.category.adapter.out.persistence.CategoryImageDocument;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImage;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageSize;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageUrl;
import vn.edu.uit.msshop.product.category.domain.model.CategoryName;

@Component
public class CategoryPersistenceMapper {
    public Category toDomain(
            final CategoryDocument entity) {
        final var id = new CategoryId(entity.getId());
        final var name = new CategoryName(entity.getName());

        final var imageDoc = entity.getImage();
        final CategoryImage image;
        if (imageDoc != null) {
            image = CategoryPersistenceMapper.toImageOrNull(
                    imageDoc.getUrl(),
                    imageDoc.getKey(),
                    imageDoc.getWidth(),
                    imageDoc.getHeight());
        } else {
            image = null;
        }

        return new Category(id, name, image);
    }

    public CategoryDocument toPersistence(
            final Category category) {
        final var image = category.getImage();

        final CategoryImageDocument imageDoc;
        if (image != null) {
            imageDoc = new CategoryImageDocument(
                    image.url().value(),
                    image.key().value(),
                    image.size().width(),
                    image.size().height());
        } else {
            imageDoc = null;
        }

        return new CategoryDocument(
                category.getId().value(),
                category.getName().value(),
                imageDoc);
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

        return CategoryPersistenceMapper.toImage(url, key, width, height);
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
