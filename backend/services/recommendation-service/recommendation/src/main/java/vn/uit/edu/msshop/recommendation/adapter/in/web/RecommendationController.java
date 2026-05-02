package vn.uit.edu.msshop.recommendation.adapter.in.web;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.recommendation.adapter.in.web.mapper.VariantWebMapper;
import vn.uit.edu.msshop.recommendation.adapter.in.web.request.GetRecommendationRequest;
import vn.uit.edu.msshop.recommendation.adapter.in.web.response.AIServerResponse;
import vn.uit.edu.msshop.recommendation.adapter.in.web.response.VariantResponse;
import vn.uit.edu.msshop.recommendation.application.port.in.FindVariantUseCase;
import vn.uit.edu.msshop.recommendation.application.port.in.GetMLResultUseCase;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendation")
public class RecommendationController {
    private final FindVariantUseCase findUseCase;
    private final VariantWebMapper mapper;
    private final GetMLResultUseCase getMLResultUseCase;

    @PostMapping("/")
    public ResponseEntity<List<VariantResponse>> getRecommendation(@RequestBody GetRecommendationRequest request) {
        final var result = findUseCase.findByTarget(request.getAge(), request.getGender(), request.getShape(), request.getBodyShape());
        final var response = result.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }
    @PostMapping(
        value = "/predict", 
        consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE},
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AIServerResponse> getPrediction(@RequestBody byte[] imageBytes){
        return ResponseEntity.ok(getMLResultUseCase.getResponse(imageBytes));
    }

}
