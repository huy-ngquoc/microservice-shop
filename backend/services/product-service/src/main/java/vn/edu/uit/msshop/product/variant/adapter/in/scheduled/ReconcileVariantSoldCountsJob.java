package vn.edu.uit.msshop.product.variant.adapter.in.scheduled;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.variant.application.port.in.command.ReconcileVariantSoldCountsUseCase;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReconcileVariantSoldCountsJob {
    private final ReconcileVariantSoldCountsUseCase reconcileVariantSoldCountsUseCase;

    @Scheduled(
            fixedRate = 2,
            timeUnit = TimeUnit.MINUTES)
    public void reconcile() {
        try {
            this.reconcileVariantSoldCountsUseCase.execute();
        } catch (final RuntimeException e) {
            log.warn("Sold count reconciliation skipped: {}", e.getMessage());
        }
    }
}
