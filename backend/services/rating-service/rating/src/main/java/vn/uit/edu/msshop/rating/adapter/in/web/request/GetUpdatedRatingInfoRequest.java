package vn.uit.edu.msshop.rating.adapter.in.web.request;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUpdatedRatingInfoRequest {
    private Instant start;
    private Instant end;
}
