package vn.uit.edu.msshop.rating.application.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.rating.application.dto.query.RatingView;
import vn.uit.edu.msshop.rating.domain.model.Rating;

@Component
public class RatingViewMapper {
    public RatingView toView(Rating rating) {
        return new RatingView(rating.getId().value(), rating.getContent().value(), rating.getMedia().url(), rating.getMedia().type(),rating.getMedia().publicId(), rating.getProductId().value(), rating.getUserId().value(), rating.getUsername().value(),rating.getUserAvatar().value());
    }
}
