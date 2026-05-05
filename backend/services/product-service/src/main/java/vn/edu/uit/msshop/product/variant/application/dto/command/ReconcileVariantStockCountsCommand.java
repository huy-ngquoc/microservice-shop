package vn.edu.uit.msshop.product.variant.application.dto.command;

import java.time.Instant;

public record ReconcileVariantStockCountsCommand(Instant rangeStartTime,Instant rangeEndTime){}
