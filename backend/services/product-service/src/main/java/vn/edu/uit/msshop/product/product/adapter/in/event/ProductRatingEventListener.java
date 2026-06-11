package vn.edu.uit.msshop.product.product.adapter.in.event;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.adapter.in.event.payload.RatingCreatedIntegrationEvent;
import vn.edu.uit.msshop.product.product.adapter.in.event.payload.RatingDeletedIntegrationEvent;
import vn.edu.uit.msshop.product.product.adapter.in.event.payload.RatingUpdatedIntegrationEvent;
import vn.edu.uit.msshop.product.product.application.dto.command.rating.ProductRatingCreatedEventApplyCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.rating.ProductRatingDeletedEventApplyCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.rating.ProductRatingUpdatedEventApplyCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingCreatedEventApplyUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingDeletedEventApplyUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingUpdatedEventApplyUseCase;

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
        final var command = new ProductRatingCreatedEventApplyCommand(
                event.eventId(),
                event.productId(),
                event.point());
        this.ratingCreatedEventApplyUseCase.apply(command);
    }

    @KafkaHandler
    public void onUpdate(
            final RatingUpdatedIntegrationEvent event) {
        final var command = new ProductRatingUpdatedEventApplyCommand(
                event.eventId(),
                event.productId(),
                event.oldPoint(),
                event.newPoint());
        this.ratingUpdatedEventApplyUseCase.apply(command);
    }

    @KafkaHandler
    public void onDeleted(
            final RatingDeletedIntegrationEvent event) {
        final var command = new ProductRatingDeletedEventApplyCommand(
                event.eventId(),
                event.productId(),
                event.point());
        this.ratingDeletedEventApplyUseCase.apply(command);
    }
}
