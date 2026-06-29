package vn.edu.uit.msshop.product.variant.adapter.in.scheduler;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import vn.edu.uit.msshop.product.variant.application.port.in.command.count.VariantSoldCountBulkReconciliationUseCase;

@Component
@RequiredArgsConstructor
@Slf4j
public class VariantSoldCountReconciliationJob {

    private final VariantSoldCountBulkReconciliationUseCase soldCountBulkReconciliationUSeCase;

    @Scheduled(
            fixedRate = 2,
            timeUnit = TimeUnit.HOURS)
    @SchedulerLock(
            name = "variantSoldCountReconciliation",
            lockAtMostFor = "PT30M",
            lockAtLeastFor = "PT1M")
    public void reconcile() {
        try {
            this.soldCountBulkReconciliationUSeCase.reconcileAll();
        } catch (final RuntimeException e) {
            log.warn("Sold count reconciliation skipped: {}", e.getMessage());
        }
    }
}
