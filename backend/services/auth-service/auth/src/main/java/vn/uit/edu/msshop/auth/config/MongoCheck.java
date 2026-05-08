package vn.uit.edu.msshop.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MongoCheck implements CommandLineRunner {
    @Autowired
    private Environment env;

    @Override
    public void run(String... args) {
        // Đây mới là cách lấy giá trị từ application.yml
        System.err.println(">>>> URI THỰC TẾ: " + env.getProperty("spring.mongodb.uri"));
    }
}
