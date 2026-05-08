package vn.uit.edu.msshop.rating.application.port.out;

import vn.uit.edu.msshop.rating.domain.event.RatingDeleted;
import vn.uit.edu.msshop.rating.domain.event.RatingPosted;
import vn.uit.edu.msshop.rating.domain.event.RatingUpdated;

public interface PublishRatingEvent {
    public void publish(RatingPosted ratingPosted);
    public void publish(RatingUpdated ratingUpdated);
    public void publish(RatingDeleted ratingDeleted);
}
