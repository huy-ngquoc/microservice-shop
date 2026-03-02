package vn.edu.uit.msshop.product.domain.model.category.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

final class CategoryImageUrlTest {

    @Test
    @DisplayName("Throw IllegalArgumentException when instantiated with null URL")
    void constructor_NullValue_ThrowsException() {
        // When & Then
        Assertions.assertThatThrownBy(() -> new CategoryImageUrl(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("imageUrl null");
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "ftp://example.com/image.png",
                    "www.example.com/image.png",
                    "images/category.png",
                    "   ",
                    ""
            })
    @DisplayName("Throw IllegalArgumentException when URL does not start with 'http'")
    void constructor_InvalidPrefix_ThrowsException(
            final String invalidUrl) {
        // When & Then
        Assertions.assertThatThrownBy(() -> new CategoryImageUrl(invalidUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("imageUrl invalid");
    }

    @Test
    @DisplayName("Trim whitespace and successfully instantiate when a valid URL has leading/trailing spaces")
    void constructor_ValidUrlWithWhitespace_TrimsAndSucceeds() {
        // Given
        final var rawUrl = "   https://example.com/images/cat1.png   ";

        // When
        final var imageUrl = new CategoryImageUrl(rawUrl);

        // Then
        Assertions.assertThat(imageUrl.value())
                .as("The URL should be trimmed of leading and trailing whitespaces")
                .isEqualTo("https://example.com/images/cat1.png");
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "http://example.com/image.jpg",
                    "https://example.com/image.png"
            })
    @DisplayName("Successfully instantiate when a valid http or https URL is provided")
    void constructor_ValidUrl_Success(
            final String validUrl) {
        // When
        final var imageUrl = new CategoryImageUrl(validUrl);

        // Then
        Assertions.assertThat(imageUrl.value())
                .as("The internal value should exactly match the provided valid URL")
                .isEqualTo(validUrl);
    }
}
