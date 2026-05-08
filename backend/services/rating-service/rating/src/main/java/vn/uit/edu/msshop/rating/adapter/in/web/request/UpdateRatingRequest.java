package vn.uit.edu.msshop.rating.adapter.in.web.request;

import java.util.UUID;

import vn.uit.edu.msshop.rating.adapter.in.web.request.common.ChangeRequest;

public record UpdateRatingRequest(
    UUID id,
    ChangeRequest<String> content,
    ChangeRequest<Float> ratingPoint
    

) {
    
}
