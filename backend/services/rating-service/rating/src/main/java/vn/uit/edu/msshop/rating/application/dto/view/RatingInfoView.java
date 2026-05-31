package vn.uit.edu.msshop.rating.application.dto.view;

import java.time.Instant;
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
    private long ratingCount;
    private long totalPoint;
    private Instant createAt;
    private Instant updateAt;
}
