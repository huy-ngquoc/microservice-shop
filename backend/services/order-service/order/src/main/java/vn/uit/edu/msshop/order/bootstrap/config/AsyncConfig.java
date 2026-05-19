package vn.uit.edu.msshop.order.bootstrap.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(
            name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // Số lượng Thread tối thiểu luôn chạy
        executor.setCorePoolSize(10);
        // Số lượng Thread tối đa nếu hàng đợi bị đầy
        executor.setMaxPoolSize(20);
        // Hàng đợi chứa các task chờ xử lý
        executor.setQueueCapacity(500);
        // Tên tiền tố để dễ debug trong log
        executor.setThreadNamePrefix("OrderCleaner-");
        executor.initialize();
        return executor;
    }
}
