package vn.edu.uit.msshop.product.product.adapter.in.scheduler;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.application.dto.command.rating.ReconcileProductRatingsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingBulkReconciliationUseCase;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReconcileProductRatingsJob {
    private static final Duration WINDOW_DURATION = Duration.ofHours(24);

    private final ProductRatingBulkReconciliationUseCase ratingBulkReconciliationUseCase;

    @Scheduled(
            fixedRate = 2,
            timeUnit = TimeUnit.HOURS)
    public void run() {
        final var rangeEndTime = Instant.now();
        final var rangeStartTime = rangeEndTime.minus(WINDOW_DURATION);

        final var command = new ReconcileProductRatingsCommand(rangeStartTime, rangeEndTime);

        try {
            ratingBulkReconciliationUseCase.execute(command);
        } catch (final RuntimeException e) {
            log.warn("Product rating reconciliation skipped: {}", e.getMessage());
        }
    }
}
