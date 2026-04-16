package vn.uit.edu.msshop.rating.application.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.rating.application.dto.query.RatingInfoView;
import vn.uit.edu.msshop.rating.domain.model.RatingInfo;

@Component
public class RatingInfoViewMapper {
    public RatingInfoView toView(RatingInfo domain) {
        return new RatingInfoView(domain.getProductId().value(), domain.getRatingCount().value(), domain.getTotalPoint().value());
    }
}
