package vn.edu.uit.msshop.product.variant.adapter.in.scheduler;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import vn.edu.uit.msshop.product.variant.application.port.in.command.reconciliation.VariantStateBulkReconciliationUseCase;

@Component
@RequiredArgsConstructor
@Slf4j
public class VariantStateBulkReconciliationJob {

    private final VariantStateBulkReconciliationUseCase bulkReconciliationUseCase;

    @Scheduled(
            fixedRate = 6,
            timeUnit = TimeUnit.HOURS)
    @SchedulerLock(
            name = "variantStateBulkReconciliation",
            lockAtMostFor = "PT1H",
            lockAtLeastFor = "PT1M")
    public void reconcile() {
        try {
            this.bulkReconciliationUseCase.reconcileAll();
        } catch (final RuntimeException exception) {
            log.warn("Skipped variant-state reconciliation: {}", exception.getMessage());
        }
    }

}
