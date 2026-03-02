package vn.edu.uit.msshop.product.domain.model.category.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class CategoryImageTest {

    // Dummy valid data for testing
    private final CategoryImageUrl validUrl = new CategoryImageUrl("https://example.com/img.jpg");
    private final CategoryImageKey validKey = new CategoryImageKey("img-123");
    private final CategoryImageSize validSize = new CategoryImageSize(800, 600);

    @Test
    @DisplayName("Throw IllegalArgumentException when CategoryImageUrl is null")
    void constructor_NullUrl_ThrowsException() {
        // When & Then
        Assertions.assertThatThrownBy(() -> new CategoryImage(null, validKey, validSize))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Throw IllegalArgumentException when CategoryImageKey is null")
    void constructor_NullKey_ThrowsException() {
        // When & Then
        Assertions.assertThatThrownBy(() -> new CategoryImage(validUrl, null, validSize))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Throw IllegalArgumentException when CategoryImageSize is null")
    void constructor_NullSize_ThrowsException() {
        // When & Then
        Assertions.assertThatThrownBy(() -> new CategoryImage(validUrl, validKey, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Successfully instantiate when all fields are valid and non-null")
    void constructor_ValidFields_Success() {
        // When
        CategoryImage image = new CategoryImage(validUrl, validKey, validSize);

        // Then
        Assertions.assertThat(image.url()).isEqualTo(validUrl);
        Assertions.assertThat(image.key()).isEqualTo(validKey);
        Assertions.assertThat(image.size()).isEqualTo(validSize);
    }
}
