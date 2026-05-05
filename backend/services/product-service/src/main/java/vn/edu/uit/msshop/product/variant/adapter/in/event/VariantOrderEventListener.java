package vn.edu.uit.msshop.product.variant.adapter.in.event;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.in.event.payload.SetVariantSoldCountsEvent;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SetAllVariantSoldCountsUseCase;
import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantOrderSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCountValue;

@Component
@KafkaListener(topics = "order-variant")
@RequiredArgsConstructor
public class VariantOrderEventListener {
  private final SetAllVariantSoldCountsUseCase setAllUseCase;

  @KafkaHandler
  public void onSetSoldCounts(final SetVariantSoldCountsEvent event) {
    final var orderSoldCounts =
        event.details().stream().map(VariantOrderEventListener::toOrderSoldCount).toList();

    this.setAllUseCase.execute(orderSoldCounts);
  }

  private static VariantOrderSoldCount toOrderSoldCount(
      final SetVariantSoldCountsEvent.Detail detail) {
    return new VariantOrderSoldCount(new VariantId(detail.variantId()),
        new VariantSoldCountValue(detail.newTotal()));
  }
}
