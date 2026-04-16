package vn.uit.edu.msshop.rating.adapter.in.web.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingInfoResponse {
    private UUID productId;
    private int ratingCount;
    private float totalPoint;
}
