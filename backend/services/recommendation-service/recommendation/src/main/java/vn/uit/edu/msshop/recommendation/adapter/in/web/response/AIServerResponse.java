package vn.uit.edu.msshop.recommendation.adapter.in.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AIServerResponse {
    private String status;
    private BodyShapeResponse prediction;
}
