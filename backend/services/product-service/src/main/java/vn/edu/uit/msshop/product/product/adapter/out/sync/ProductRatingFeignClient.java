package vn.edu.uit.msshop.product.product.adapter.out.sync;

import java.time.Instant;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.edu.uit.msshop.product.product.adapter.out.sync.response.RatingInfoResponse;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@FeignClient(
        name = "rating-service")
public interface ProductRatingFeignClient {
    @GetMapping("/rating/public/updated-rating-info")
    PageResponseDto<RatingInfoResponse> getUpdatedRatingInfos(
            @RequestParam
            final Instant rangeStartTime,

            @RequestParam
            final Instant rangeEndTime,

            @RequestParam
            final int page,

            @RequestParam
            final int size);
}
