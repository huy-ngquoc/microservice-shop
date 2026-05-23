package vn.uit.edu.msshop.rating.application.port.out;

import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.uit.edu.msshop.rating.application.dto.query.ListRatingInfosQuery;
import vn.uit.edu.msshop.rating.domain.model.RatingInfo;

public interface ListRatingInfosPort {
    PageResponseDto<RatingInfo> list(
            ListRatingInfosQuery query);
}
