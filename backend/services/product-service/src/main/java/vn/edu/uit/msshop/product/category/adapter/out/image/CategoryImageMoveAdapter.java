package vn.edu.uit.msshop.product.category.adapter.out.image;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.category.application.port.out.MoveCategoryImagePort;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryImageMoveAdapter implements MoveCategoryImagePort {
    private static final String TEMP_FOLDER = "temp";
    private static final String CATEGORY_FOLDER = "categories";

    private final Cloudinary cloudinary;

    @Override
    public void moveToCategory(
            final CategoryImageKey key) {
        final var fromPublicId = TEMP_FOLDER + "/" + key.value();
        final var toPublicId = CATEGORY_FOLDER + "/" + key.value();

        this.renamePublicId(fromPublicId, toPublicId);
    }

    @Override
    public void moveBackToTemp(
            final CategoryImageKey key) {
        final var fromPublicId = CATEGORY_FOLDER + "/" + key.value();
        final var toPublicId = TEMP_FOLDER + "/" + key.value();

        this.renamePublicId(fromPublicId, toPublicId);
    }

    @Override
    public void deleteFromCategory(
            final CategoryImageKey key) {
        try {
            this.cloudinary.uploader().destroy(CATEGORY_FOLDER + "/" + key.value(), Map.of());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete image: " + key.value(), e);
        }
    }

    private void renamePublicId(
            final String fromPublicId,
            final String toPublicId) {
        try {
            this.cloudinary.uploader().rename(fromPublicId, toPublicId, Map.of());
        } catch (Exception e) {
            throw new RuntimeException("Failed to rename image: " + fromPublicId + " → " + toPublicId, e);
        }
    }
}
