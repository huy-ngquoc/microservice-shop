package vn.uit.edu.msshop.rating.application.dto.query;

import java.time.Instant;
import java.util.Objects;

import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;

public record ListRatingInfosQuery(
        PageRequestDto pageRequest,
        Instant start,
        Instant end) {
    public ListRatingInfosQuery {
        Objects.requireNonNull(pageRequest);
    }
}
