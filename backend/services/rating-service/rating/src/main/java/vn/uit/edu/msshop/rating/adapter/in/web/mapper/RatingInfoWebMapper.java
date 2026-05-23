package vn.uit.edu.msshop.rating.adapter.in.web.mapper;

import java.time.Instant;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.uit.edu.msshop.rating.adapter.in.web.response.RatingInfoResponse;
import vn.uit.edu.msshop.rating.application.dto.query.ListRatingInfosQuery;
import vn.uit.edu.msshop.rating.application.dto.view.RatingInfoView;

@Component
public class RatingInfoWebMapper {
    public RatingInfoResponse toResponse(
            final RatingInfoView view) {
        return new RatingInfoResponse(
                view.getProductId(),
                view.getRatingCount(),
                view.getTotalPoint(),
                view.getCreateAt(),
                view.getUpdateAt());
    }

    public ListRatingInfosQuery toListQuery(
            final int page,

            final int size,

            @Nullable
            final String sortBy,

            final PageRequestDto.Direction direction,

            final Instant start,

            final Instant end) {
        final var pageRequest = new PageRequestDto(
                page,
                size,
                sortBy,
                direction);

        return new ListRatingInfosQuery(
                pageRequest,
                start,
                end);
    }
}
