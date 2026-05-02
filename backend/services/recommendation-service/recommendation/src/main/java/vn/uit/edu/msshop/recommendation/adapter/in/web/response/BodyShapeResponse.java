
package vn.uit.edu.msshop.recommendation.adapter.in.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BodyShapeResponse {
    private String gender;
    @JsonProperty("body_shape")
    private String bodyShape;
}
