package vn.edu.uit.msshop.product.product.adapter.in.scheduler;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import vn.edu.uit.msshop.product.product.application.dto.command.rating.ProductRatingBulkReconciliationCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingBulkReconciliationUseCase;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldNameConstants
public class ProductRatingBulkReconciliationJob {

    private static final Duration WINDOW_DURATION = Duration.ofHours(24);

    private final ProductRatingBulkReconciliationUseCase ratingBulkReconciliationUseCase;

    @Scheduled(
            fixedRate = 2,
            timeUnit = TimeUnit.HOURS)
    @SchedulerLock(
            name = "productRatingBulkReconciliation",
            lockAtMostFor = "PT30M",
            lockAtLeastFor = "PT1M")
    public void run() {
        final var rangeEndTime = Instant.now();
        final var rangeStartTime = rangeEndTime.minus(WINDOW_DURATION);

        final var command = new ProductRatingBulkReconciliationCommand(rangeStartTime, rangeEndTime);

        try {
            ratingBulkReconciliationUseCase.reconcileAll(command);
        } catch (final RuntimeException e) {
            log.warn("Product rating reconciliation skipped: {}", e.getMessage());
        }
    }
}
