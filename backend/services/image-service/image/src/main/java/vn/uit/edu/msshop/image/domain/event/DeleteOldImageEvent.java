package vn.uit.edu.msshop.image.domain.event;

import java.util.UUID;

public record DeleteOldImageEvent(String oldImagePublicId, UUID eventId) {

}
