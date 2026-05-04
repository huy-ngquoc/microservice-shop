package vn.edu.uit.msshop.product.variant.adapter.in.scheduled;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.variant.application.dto.command.ReconcileVariantStockCountsCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.ReconcileVariantStockCountsUseCase;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReconcileVariantStockCountsJob {
    private static final int RATE_IN_HOURS = 24;

    private final ReconcileVariantStockCountsUseCase reconcileUseCase;

    @Scheduled(
            fixedRate = RATE_IN_HOURS,
            timeUnit = TimeUnit.HOURS)
    public void reconcile() {
        final var rangeEndTime = Instant.now();
        final var rangeStartTime = rangeEndTime.minus(Duration.ofHours(RATE_IN_HOURS));

        final var command = new ReconcileVariantStockCountsCommand(
                rangeStartTime,
                rangeEndTime);

        try {
            this.reconcileUseCase.execute(command);
        } catch (final RuntimeException e) {
            log.warn("Stock count reconciliation skipped: {}", e.getMessage());
        }
    }
}
