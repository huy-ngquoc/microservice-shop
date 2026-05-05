package vn.edu.uit.msshop.product.category.domain.model;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;

final class CategoryIdTest {
    @Test
    @DisplayName("Throw DomainException when instantiated with null UUID")
    @SuppressWarnings("NullAway")
    void constructor_NullValue_ThrowsException() {
        // When & Then
        Assertions.assertThatThrownBy(() -> new CategoryId(null))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("Successfully instantiate when a valid UUID is provided")
    void constructor_ValidUuid_Success() {
        // Given
        final var validUuid = UUID.fromString("00000000-000-0000-0000-000000000001");

        // When
        final var categoryId = new CategoryId(validUuid);

        // Then
        Assertions.assertThat(categoryId.value())
                .as("The internal UUID value should match the provided UUID")
                .isEqualTo(validUuid);
    }

    @Test
    @DisplayName("newId() should generate a new CategoryId with a non-null UUID")
    void newId_GeneratesValidCategoryId() {
        // When
        final var categoryId1 = CategoryId.newId();
        final var categoryId2 = CategoryId.newId();

        // Then
        Assertions.assertThat(categoryId1).isNotNull();
        Assertions.assertThat(categoryId1.value()).isNotNull();

        // (Optional) Ensure each call generates a unique UUID
        Assertions.assertThat(categoryId1.value())
                .as("Auto-generated IDs should be unique")
                .isNotEqualTo(categoryId2.value());
    }
}
