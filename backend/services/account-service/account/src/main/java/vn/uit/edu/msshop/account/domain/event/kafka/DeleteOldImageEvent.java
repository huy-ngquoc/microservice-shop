package vn.uit.edu.msshop.account.domain.event.kafka;

import java.util.UUID;

public record DeleteOldImageEvent(String oldImagePublicId, UUID eventId) {

}
