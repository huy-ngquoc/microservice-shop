package vn.edu.uit.msshop.product.config.properties;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource(
        properties = {
                "cloudinary.cloud-name=my-test-cloud",
                "cloudinary.api-key=my-test-api-key",
                "cloudinary.api-secret=my-test-api-secret"
        })
class CloudinaryPropertiesTest {

    // Tạo một context Spring siêu nhỏ chỉ load đúng cái class Record này để test
    // cho lẹ
    @Configuration
    @EnableConfigurationProperties(CloudinaryProperties.class)
    static class TestConfig {
    }

    private final CloudinaryProperties cloudinaryProperties;

    @Autowired
    CloudinaryPropertiesTest(
            final CloudinaryProperties cloudinaryProperties) {
        this.cloudinaryProperties = cloudinaryProperties;
    }

    @Test
    @DisplayName("Should bind properties from configuration to CloudinaryProperties record correctly")
    void shouldBindPropertiesCorrectly() {
        // Kiểm tra xem Spring có tạo được Bean và tiêm (inject) vào đây không
        Assertions.assertThat(this.cloudinaryProperties).isNotNull();

        // Kiểm tra xem dữ liệu map từ "cloud-name" sang "cloudName" có chuẩn xác không
        Assertions.assertThat(this.cloudinaryProperties.cloudName())
                .as("Cloud name should be mapped correctly")
                .isEqualTo("my-test-cloud");

        Assertions.assertThat(this.cloudinaryProperties.apiKey())
                .as("API key should be mapped correctly")
                .isEqualTo("my-test-api-key");

        Assertions.assertThat(this.cloudinaryProperties.apiSecret())
                .as("API secret should be mapped correctly")
                .isEqualTo("my-test-api-secret");
    }
}
