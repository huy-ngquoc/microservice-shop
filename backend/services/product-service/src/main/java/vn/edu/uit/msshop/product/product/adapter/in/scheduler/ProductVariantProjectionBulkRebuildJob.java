package vn.edu.uit.msshop.product.product.adapter.in.scheduler;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantProjectionBulkRebuildUseCase;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductVariantProjectionBulkRebuildJob {

    private final ProductVariantProjectionBulkRebuildUseCase bulkRebuildUseCase;

    @Scheduled(
            fixedRate = 6,
            timeUnit = TimeUnit.HOURS)
    public void rebuild() {
        try {
            this.bulkRebuildUseCase.rebuildAll();
        } catch (final RuntimeException e) {
            log.warn("Skipped product-variant projection rebuild: {}", e.getMessage());
        }
    }
}
