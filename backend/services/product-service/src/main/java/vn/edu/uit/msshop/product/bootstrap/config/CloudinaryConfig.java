package vn.edu.uit.msshop.product.bootstrap.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

import vn.edu.uit.msshop.product.bootstrap.config.properties.CloudinaryProperties;

@Configuration
@EnableConfigurationProperties(CloudinaryProperties.class)
public class CloudinaryConfig {
    @Bean
    Cloudinary cloudinary(
            CloudinaryProperties properties) {
        final var url = String.format(
                "cloudinary://%s:%s@%s",
                properties.cloudName(),
                properties.apiKey(),
                properties.apiSecret());

        return new Cloudinary(url);
    }
}
