package vn.edu.uit.msshop.product.variant.application.dto.integration;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record VariantUpdatedIntegrationEvent(UUID eventId,UUID variantId,List<String>traits,long unitPrice,String name,@Nullable String imageKey)implements VariantIntegrationEvent{public VariantUpdatedIntegrationEvent{traits=List.copyOf(traits);}

@Override public String getAggregateId(){return variantId.toString();}}
