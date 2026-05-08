package vn.uit.edu.msshop.recommendation.adapter.remote;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.uit.edu.msshop.recommendation.adapter.in.web.request.PageRequestDto;
import vn.uit.edu.msshop.recommendation.adapter.in.web.response.PageResponseDto;
import vn.uit.edu.msshop.recommendation.adapter.in.web.response.VariantResponse;

@FeignClient("product-service")
public interface ProductCaller {
    @GetMapping("/variants")
    public ResponseEntity<PageResponseDto<VariantResponse>> list(
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

            @RequestParam(
                    name = "target",
                    required = false)
            @Nullable
            final List<String> targets);
}
