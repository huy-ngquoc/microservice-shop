package vn.edu.uit.msshop.product.product.adapter.in.event;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.adapter.in.event.payload.RatingCreatedIntegrationEvent;
import vn.edu.uit.msshop.product.product.adapter.in.event.payload.RatingDeletedIntegrationEvent;
import vn.edu.uit.msshop.product.product.adapter.in.event.payload.RatingUpdatedIntegrationEvent;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingCreatedEventApplyUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingDeletedEventApplyUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingUpdatedEventApplyUseCase;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Component
@RequiredArgsConstructor
@Slf4j
@KafkaListener(
        topics = "rating-product")
public class ProductRatingEventListener {
    private final ProductRatingCreatedEventApplyUseCase ratingCreatedEventApplyUseCase;
    private final ProductRatingUpdatedEventApplyUseCase ratingUpdatedEventApplyUseCase;
    private final ProductRatingDeletedEventApplyUseCase ratingDeletedEventApplyUseCase;

    @KafkaHandler
    public void onCreated(
            final RatingCreatedIntegrationEvent event) {
        this.ratingCreatedEventApplyUseCase.execute(
                event.eventId(),
                new ProductId(event.productId()),
                event.point());
    }

    @KafkaHandler
    public void onUpdate(
            final RatingUpdatedIntegrationEvent event) {
        this.ratingUpdatedEventApplyUseCase.execute(
                event.eventId(),
                new ProductId(event.productId()),
                event.oldPoint(),
                event.newPoint());
    }

    @KafkaHandler
    public void onDeleted(
            final RatingDeletedIntegrationEvent event) {
        this.ratingDeletedEventApplyUseCase.execute(
                event.eventId(),
                new ProductId(event.productId()),
                event.point());
    }
}
