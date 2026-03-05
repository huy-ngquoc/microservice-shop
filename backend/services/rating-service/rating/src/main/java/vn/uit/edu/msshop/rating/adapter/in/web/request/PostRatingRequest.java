package vn.uit.edu.msshop.rating.adapter.in.web.request;


import java.util.UUID;

public record PostRatingRequest(
   
    UUID ratingId,
    String content,
    UUID productId,
    UUID userId,
    String userName,
    String userAvatar,
    float ratingPoint

) {

}
