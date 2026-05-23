package vn.uit.edu.msshop.rating.application.port.in;

import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.uit.edu.msshop.rating.application.dto.query.ListRatingInfosQuery;
import vn.uit.edu.msshop.rating.application.dto.view.RatingInfoView;

public interface ListRatingInfosUseCase {
    PageResponseDto<RatingInfoView> list(
            final ListRatingInfosQuery query);
}
