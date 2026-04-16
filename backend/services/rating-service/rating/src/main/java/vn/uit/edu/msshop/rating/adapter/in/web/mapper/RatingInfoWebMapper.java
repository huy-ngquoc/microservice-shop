package vn.uit.edu.msshop.rating.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.rating.adapter.in.web.response.RatingInfoResponse;
import vn.uit.edu.msshop.rating.application.dto.query.RatingInfoView;

@Component
public class RatingInfoWebMapper {
    public RatingInfoResponse toResponse(RatingInfoView view) {
        return new RatingInfoResponse(view.getProductId(), view.getRatingCount(), view.getTotalPoint());
    }
}
