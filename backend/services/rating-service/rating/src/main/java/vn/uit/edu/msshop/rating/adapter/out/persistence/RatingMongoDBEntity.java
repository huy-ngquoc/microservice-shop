package vn.uit.edu.msshop.rating.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
@Document(collection = "ratings")
public class RatingMongoDBEntity {
    @Id 
    private UUID id;

    @Field("product_id")
    private UUID productId;

    @Field("user_id")
    private UUID userId;

    private String content;

    private MediaDocument media; 

    @Field("user_name")
    private String userName;

    @Field("rating_point")
    private float ratingPoint;

    @Field("user_avatar")
    private String userAvatar;
}
