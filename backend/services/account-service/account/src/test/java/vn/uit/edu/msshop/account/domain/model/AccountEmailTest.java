package vn.uit.edu.msshop.account.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import vn.uit.edu.msshop.account.domain.model.valueobject.AccountEmail;

public class AccountEmailTest {
    @Test
    @DisplayName("Nên khởi tạo thành công khi email hợp lệ")
    void shouldCreateAccountEmailWhenValueIsValid() {
        // Given
        String validEmail = "test.user@example.com";

        // When
        AccountEmail accountEmail = new AccountEmail(validEmail);

        // Then
        assertNotNull(accountEmail);
        assertEquals(validEmail, accountEmail.value());
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "user@domain.com",
                    "user.name+tag+sorting@example.com",
                    "x@example.com",
                    "user@sub.domain.com",
                    "1234567890@example.com"
            })
    @DisplayName("Nên khởi tạo thành công với danh sách các định dạng email hợp lệ")
    void shouldCreateAccountEmailWithVariousValidFormats(
            String validEmail) {
        AccountEmail accountEmail = new AccountEmail(validEmail);
        assertEquals(validEmail, accountEmail.value());
    }

    @Test
    @DisplayName("Nên ném ngoại lệ IllegalArgumentException khi email bị null")
    void shouldThrowExceptionWhenEmailIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AccountEmail(null));

        assertEquals("Invalid email", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "", // Chuỗi rỗng
                    "   ", // Khoảng trắng
                    "plainaddress", // Thiếu ký tự @ và domain
                    "@missing-local.com", // Thiếu phần tên trước @
                    "user@.com", // Thiếu tên domain chính
                    "user@domain.", // Thiếu phần mở rộng (TLD)
                    "user@domain..com", // Nhân đôi dấu chấm
                    "user @domain.com" // Chứa khoảng trắng ở giữa
            })
    @DisplayName("Nên ném ngoại lệ IllegalArgumentException khi định dạng email sai")
    void shouldThrowExceptionWhenEmailFormatIsInvalid(
            String invalidEmail) {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AccountEmail(invalidEmail));

        assertEquals("Invalid email", exception.getMessage());
    }

}
