package vn.uit.edu.msshop.recommendation.adapter.in.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetRecommendationRequest {
    private String age;
    private String gender;
    private String shape;
    private String bodyShape;
}
