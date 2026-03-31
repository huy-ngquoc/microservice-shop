package vn.uit.edu.msshop.account.domain.event.kafka;

import java.util.UUID;

public record RollbackImageEvent(String imagePublicId, UUID eventId) {

}
