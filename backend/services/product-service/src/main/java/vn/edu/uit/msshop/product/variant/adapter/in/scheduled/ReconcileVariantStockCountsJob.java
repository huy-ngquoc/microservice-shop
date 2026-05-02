package vn.edu.uit.msshop.product.variant.adapter.in.scheduled;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.variant.application.port.in.command.ReconcileVariantStockCountsUseCase;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReconcileVariantStockCountsJob {
    private final ReconcileVariantStockCountsUseCase reconcileUseCase;

    @Scheduled(
            fixedRate = 1,
            timeUnit = TimeUnit.HOURS)
    public void reconcile() {
        try {
            this.reconcileUseCase.execute();
        } catch (final RuntimeException e) {
            log.warn("Stock count reconciliation skipped: {}", e.getMessage());
        }
    }
}
