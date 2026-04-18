package vn.edu.uit.msshop.product.variant.adapter.in.scheduled;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.port.in.command.ReconcileVariantSoldCountsUseCase;

@Component
@RequiredArgsConstructor
public class ReconcileVariantSoldCountsJob {
    private final ReconcileVariantSoldCountsUseCase reconcileVariantSoldCountsUseCase;

    @Scheduled(
            fixedRate = 1,
            timeUnit = TimeUnit.HOURS)
    public void reconcile() {
        this.reconcileVariantSoldCountsUseCase.execute();
    }
}