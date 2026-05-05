package vn.edu.uit.msshop.product.bootstrap.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="cloudinary")public record CloudinaryProperties(String cloudName,String apiKey,String apiSecret){}
