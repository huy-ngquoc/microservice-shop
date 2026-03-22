package vn.edu.uit.msshop.product.category.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

final class CategoryNameTest {
    @Test
    @DisplayName("Throw DomainException when instantiated with null name")
    @SuppressWarnings("NullAway")
    void constructor_NullValue_ThrowsException() {
        // When & Then
        Assertions.assertThatThrownBy(() -> new CategoryName(null))
                .isInstanceOf(DomainException.class);
    }

    @ParameterizedTest
    @ValueSource(
            strings = { "", "   ", "\t", "\n" })
    @DisplayName("Throw DomainException when name is empty or blank")
    void constructor_BlankValue_ThrowsException(
            final String blankName) {
        // When & Then
        Assertions.assertThatThrownBy(() -> new CategoryName(blankName))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("Throw DomainException when name exceeds maximum length")
    void constructor_NameExceedsMaxLength_ThrowsException() {
        // Given
        final var tooLongName = "A".repeat(CategoryName.MAX_LENGTH + 1);

        // When & Then
        Assertions.assertThatThrownBy(() -> new CategoryName(tooLongName))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("Normalize internal whitespace and trim when a valid name is provided")
    void constructor_ValidNameWithExtraWhitespace_NormalizesAndSucceeds() {
        // Given
        final var rawName = "   Food     and     Drink   ";

        // When
        final var categoryName = new CategoryName(rawName);

        // Then
        Assertions.assertThat(categoryName.value())
                .as("The name should be trimmed and internal double spaces should be normalized")
                .isEqualTo("Food and Drink");
    }

    @ParameterizedTest
    @ValueSource(
            strings = { "Food", "Drink", "Electronics and Gadgets" })
    @DisplayName("Successfully instantiate when a valid name is provided")
    void constructor_ValidName_Success(
            final String validName) {
        // When
        final var categoryName = new CategoryName(validName);

        // Then
        Assertions.assertThat(categoryName.value())
                .as("The internal value should match the provided valid name")
                .isEqualTo(validName);
    }
}
