package vn.edu.uit.msshop.product.product.adapter.in.event;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.adapter.in.event.payload.RatingCreatedIntegrationEvent;
import vn.edu.uit.msshop.product.product.adapter.in.event.payload.RatingDeletedIntegrationEvent;
import vn.edu.uit.msshop.product.product.adapter.in.event.payload.RatingUpdatedIntegrationEvent;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ApplyRatingCreatedUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ApplyRatingDeletedUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ApplyRatingUpdatedUseCase;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Component
@RequiredArgsConstructor
@Slf4j
@KafkaListener(
        topics = "rating-product")
public class ProductRatingEventListener {
    private final ApplyRatingCreatedUseCase createdUseCase;
    private final ApplyRatingUpdatedUseCase updatedUseCase;
    private final ApplyRatingDeletedUseCase deletedUseCase;

    @KafkaHandler
    public void onCreated(
            final RatingCreatedIntegrationEvent event) {
        this.createdUseCase.execute(
                event.eventId(),
                new ProductId(event.productId()),
                event.point());
    }

    @KafkaHandler
    public void onUpdate(
            final RatingUpdatedIntegrationEvent event) {
        this.updatedUseCase.execute(
                event.eventId(),
                new ProductId(event.productId()),
                event.oldPoint(),
                event.newPoint());
    }

    @KafkaHandler
    public void onDeleted(
            final RatingDeletedIntegrationEvent event) {
        this.deletedUseCase.execute(
                event.eventId(),
                new ProductId(event.productId()),
                event.point());
    }
}
