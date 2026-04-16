package vn.uit.edu.msshop.rating.application.dto.query;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingInfoView {
    private UUID productId;
    private int ratingCount;
    private float totalPoint;
}
