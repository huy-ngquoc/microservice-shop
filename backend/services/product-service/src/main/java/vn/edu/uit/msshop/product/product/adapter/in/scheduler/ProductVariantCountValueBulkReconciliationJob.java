package vn.edu.uit.msshop.product.product.adapter.in.scheduler;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductVariantCountValueBulkReconciliationUseCase;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductVariantCountValueBulkReconciliationJob {

    private final ProductVariantCountValueBulkReconciliationUseCase bulkCountValueReconciliationUseCase;

    @Scheduled(
            fixedRate = 2,
            timeUnit = TimeUnit.HOURS)
    @SchedulerLock(
            name = "productVariantCountValueBulkReconciliation",
            lockAtMostFor = "PT30M",
            lockAtLeastFor = "PT1M")
    public void reconcile() {
        try {
            this.bulkCountValueReconciliationUseCase.reconcileAll();
        } catch (final RuntimeException exception) {
            log.warn("Product count value reconciliation skipped: {}", exception.getMessage());
        }
    }
}
