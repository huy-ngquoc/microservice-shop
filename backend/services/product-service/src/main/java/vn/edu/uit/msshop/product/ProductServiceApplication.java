package vn.edu.uit.msshop.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
        scanBasePackages = {
                "vn.edu.uit.msshop.shared",
                "vn.edu.uit.msshop.product"
        })
@EnableScheduling
@EnableFeignClients
public class ProductServiceApplication {

    public static void main(
            final String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}
