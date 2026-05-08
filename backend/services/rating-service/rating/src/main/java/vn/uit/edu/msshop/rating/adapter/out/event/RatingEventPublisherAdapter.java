package vn.uit.edu.msshop.rating.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.rating.application.port.out.PublishRatingEvent;
import vn.uit.edu.msshop.rating.domain.event.RatingDeleted;
import vn.uit.edu.msshop.rating.domain.event.RatingPosted;
import vn.uit.edu.msshop.rating.domain.event.RatingUpdated;

@Component
public class RatingEventPublisherAdapter implements PublishRatingEvent {
    private final ApplicationEventPublisher publisher;
    public RatingEventPublisherAdapter(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(RatingPosted ratingPosted) {
        this.publisher.publishEvent(ratingPosted);
    }

    @Override
    public void publish(RatingUpdated ratingUpdated) {
        this.publisher.publishEvent(ratingUpdated);
    }

    @Override
    public void publish(RatingDeleted ratingDeleted) {
        this.publisher.publishEvent(ratingDeleted);
    }

}
