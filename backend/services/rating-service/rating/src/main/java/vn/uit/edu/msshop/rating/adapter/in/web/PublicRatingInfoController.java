package vn.uit.edu.msshop.rating.adapter.in.web;

import java.time.Instant;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.uit.edu.msshop.rating.adapter.in.web.mapper.RatingInfoWebMapper;
import vn.uit.edu.msshop.rating.adapter.in.web.response.RatingInfoResponse;
import vn.uit.edu.msshop.rating.application.port.in.ListRatingInfosUseCase;

@RestController
@RequestMapping("/rating/public")
@RequiredArgsConstructor
public class PublicRatingInfoController {
    private final ListRatingInfosUseCase listUseCase;
    private final RatingInfoWebMapper mapper;

    @GetMapping("/updated-rating-info")
    public ResponseEntity<PageResponseDto<RatingInfoResponse>> listUpdated(
            @RequestParam(
                    defaultValue = PageRequestDto.DEFAULT_PAGE_STRING)
            final int page,

            @RequestParam(
                    defaultValue = PageRequestDto.DEFAULT_SIZE_STRING)
            final int size,

            @RequestParam(
                    required = false)
            @Nullable
            final String sortBy,

            @RequestParam(
                    defaultValue = PageRequestDto.DEFAULT_DIRECTION_STRING)
            final PageRequestDto.Direction direction,

            @RequestParam
            final Instant rangeStartTime,

            @RequestParam
            final Instant rangeEndTime) {
        final var query = this.mapper.toListQuery(
                page,
                size,
                sortBy,
                direction,
                rangeStartTime,
                rangeEndTime);
        final var views = this.listUseCase.list(query);

        final var response = views.map(this.mapper::toResponse);
        return ResponseEntity.ok(response);
    }
}
