package vn.uit.edu.msshop.recommendation.adapter.in.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.recommendation.adapter.in.web.mapper.VariantWebMapper;
import vn.uit.edu.msshop.recommendation.adapter.in.web.request.GetRecommendationRequest;
import vn.uit.edu.msshop.recommendation.adapter.in.web.response.VariantResponse;
import vn.uit.edu.msshop.recommendation.application.port.in.FindVariantUseCase;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendation")
public class RecommendationController {
    private final FindVariantUseCase findUseCase;
    private final VariantWebMapper mapper;

    @PostMapping("/")
    public ResponseEntity<List<VariantResponse>> getRecommendation(@RequestBody GetRecommendationRequest request) {
        final var result = findUseCase.findByTarget(request.getAge(), request.getGender(), request.getShape(), request.getBodyShape());
        final var response = result.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }

}
