package vn.uit.edu.msshop.rating.application.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.rating.application.dto.query.RatingView;
import vn.uit.edu.msshop.rating.domain.model.Rating;

@Component
public class RatingViewMapper {
    public RatingView toView(Rating rating) {
        String url = rating.getMedia()!=null?rating.getMedia().url():"";
        String type = rating.getMedia()!=null?rating.getMedia().type():"";
        String publicId = rating.getMedia()!=null?rating.getMedia().publicId():"";


        return new RatingView(rating.getId().value(), rating.getContent().value(), url, type,publicId, rating.getProductId().value(), rating.getUserId().value(), rating.getUsername().value(),rating.getUserAvatar().value());
    }
}
