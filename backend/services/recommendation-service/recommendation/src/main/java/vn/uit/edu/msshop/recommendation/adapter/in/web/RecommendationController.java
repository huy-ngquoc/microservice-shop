package vn.uit.edu.msshop.recommendation.adapter.in.web;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.recommendation.adapter.in.web.request.PageRequestDto;
import vn.uit.edu.msshop.recommendation.adapter.in.web.response.AIServerResponse;
import vn.uit.edu.msshop.recommendation.adapter.in.web.response.PageResponseDto;
import vn.uit.edu.msshop.recommendation.adapter.in.web.response.VariantResponse;
import vn.uit.edu.msshop.recommendation.application.port.in.GetMLResultUseCase;
import vn.uit.edu.msshop.recommendation.application.port.in.GetVariantsByTargetUseCase;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendation")
public class RecommendationController {
    
    
    private final GetMLResultUseCase getMLResultUseCase;
    private final GetVariantsByTargetUseCase getVariantsByTargetUseCase;

    
    @PostMapping(
        value = "/predict", 
        consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE},
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AIServerResponse> getPrediction(@RequestBody byte[] imageBytes){
        return ResponseEntity.ok(getMLResultUseCase.getResponse(imageBytes));
    }

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
            final List<String> targets) {
                return ResponseEntity.ok(getVariantsByTargetUseCase.getVariantResponseByTargets(targets, page, size, sortBy, direction));
            }

}
