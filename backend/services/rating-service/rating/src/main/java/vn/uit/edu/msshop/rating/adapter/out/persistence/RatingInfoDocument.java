package vn.uit.edu.msshop.rating.adapter.out.persistence;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(
        collection = "rating_info")
@CompoundIndex(
        name = "idx_times",
        def = "{'createAt': 1, 'updateAt': 1}")
@FieldNameConstants
public class RatingInfoDocument {
    @Id
    private UUID productId;

    private long ratingCount;

    private long ratingTotal;

    private Instant createAt;

    private Instant updateAt;
}
