package vn.edu.uit.msshop.product.bootstrap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

import vn.edu.uit.msshop.product.bootstrap.config.properties.CloudinaryProperties;

@Configuration
public class CloudinaryConfig {
    @Bean
    Cloudinary cloudinary(
            CloudinaryProperties properties) {
        final var url = String.format(
                "cloudinary://%s:%s@%s",
                properties.apiKey(),
                properties.apiSecret(),
                properties.cloudName());

        return new Cloudinary(url);
    }
}
