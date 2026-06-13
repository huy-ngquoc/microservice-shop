package vn.edu.uit.msshop.product.variant.adapter.in.scheduled;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.variant.application.dto.command.count.VariantStockCountBulkReconciliationCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.count.VariantStockCountBulkReconciliationUseCase;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReconcileVariantStockCountsJob {
    private static final Duration WINDOW_DURATION = Duration.ofHours(24);

    private final VariantStockCountBulkReconciliationUseCase stockCountBulkReconciliationUseCase;

    @Scheduled(
            fixedRate = 2,
            timeUnit = TimeUnit.HOURS)
    public void reconcile() {
        final var rangeEndTime = Instant.now();
        final var rangeStartTime = rangeEndTime.minus(WINDOW_DURATION);

        final var command = new VariantStockCountBulkReconciliationCommand(rangeStartTime, rangeEndTime);

        try {
            this.stockCountBulkReconciliationUseCase.execute(command);
        } catch (final RuntimeException e) {
            log.warn("Stock count reconciliation skipped: {}", e.getMessage());
        }
    }
}
