package vn.edu.uit.msshop.product.bootstrap.config.properties;

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
        properties = { "cloudinary.cloud-name=my-test-cloud",
                "cloudinary.api-key=my-test-api-key", "cloudinary.api-secret=my-test-api-secret" })
class CloudinaryPropertiesTest {

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
        Assertions.assertThat(this.cloudinaryProperties).isNotNull();

        Assertions.assertThat(this.cloudinaryProperties.cloudName())
                .as("Cloud name should be mapped correctly").isEqualTo("my-test-cloud");

        Assertions.assertThat(this.cloudinaryProperties.apiKey())
                .as("API key should be mapped correctly").isEqualTo("my-test-api-key");

        Assertions.assertThat(this.cloudinaryProperties.apiSecret())
                .as("API secret should be mapped correctly").isEqualTo("my-test-api-secret");
    }
}
