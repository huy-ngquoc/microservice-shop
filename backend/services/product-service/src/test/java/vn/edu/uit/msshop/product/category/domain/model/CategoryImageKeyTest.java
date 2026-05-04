package vn.edu.uit.msshop.product.category.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryImageKey;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

final class CategoryImageKeyTest {

    @Test
    @DisplayName("Throw DomainException when instantiated with null key")
    @SuppressWarnings("NullAway")
    void constructor_NullValue_ThrowsException() {
        // When & Then
        Assertions.assertThatThrownBy(() -> new CategoryImageKey(null))
                .isInstanceOf(DomainException.class);
    }

    @ParameterizedTest
    @ValueSource(
            strings = { "", "   ", "\t", "\n" })
    @DisplayName("Throw DomainException when key is blank or contains only whitespaces")
    void constructor_BlankValue_ThrowsException(
            final String blankKey) {
        // When & Then
        Assertions.assertThatThrownBy(() -> new CategoryImageKey(blankKey))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("Trim whitespace and successfully instantiate when a valid key is provided")
    void constructor_ValidKey_TrimsAndSucceeds() {
        // Given
        final var rawKey = "   cloudinary-id-12345   ";

        // When
        final var key = new CategoryImageKey(rawKey);

        // Then
        Assertions.assertThat(key.value())
                .as("The key should be trimmed of leading and trailing whitespaces")
                .isEqualTo("cloudinary-id-12345");
    }
}
