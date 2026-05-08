package vn.uit.edu.msshop.image.domain.event;

import java.util.UUID;

public record RollbackImageEvent(String imagePublicId, UUID eventId) {

}
