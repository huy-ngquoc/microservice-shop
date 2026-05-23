package vn.uit.edu.msshop.rating.adapter.in.web.request;

import java.util.UUID;

import vn.edu.uit.msshop.shared.adapter.in.web.request.ChangeRequest;

public record UpdateRatingRequest(
        UUID id,
        ChangeRequest<String> content,
        ChangeRequest<Integer> ratingPoint) {
}
