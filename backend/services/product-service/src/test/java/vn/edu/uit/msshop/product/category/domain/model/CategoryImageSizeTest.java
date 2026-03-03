package vn.edu.uit.msshop.product.category.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

final class CategoryImageSizeTest {

    @ParameterizedTest
    @CsvSource({
            "0, 100", // Width is 0
            "-1, 100", // Width is negative
            "100, 0", // Height is 0
            "100, -1", // Height is negative
            "0, 0", // Both are 0
            "-1, -1" // Both are negative
    })
    @DisplayName("Throw DomainException when width or height is less than or equal to zero")
    void constructor_InvalidDimensions_ThrowsException(
            final int width,
            final int height) {
        // When & Then
        Assertions.assertThatThrownBy(() -> new CategoryImageSize(width, height))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("Successfully instantiate when both width and height are strictly positive")
    void constructor_ValidDimensions_Success() {
        // Given
        final int validWidth = 800;
        final int validHeight = 600;

        // When
        final var imageSize = new CategoryImageSize(validWidth, validHeight);

        // Then
        Assertions.assertThat(imageSize.width())
                .as("The internal width should match the provided valid width")
                .isEqualTo(validWidth);

        Assertions.assertThat(imageSize.height())
                .as("The internal height should match the provided valid height")
                .isEqualTo(validHeight);
    }
}
