package vn.edu.uit.msshop.profile.domain.model.valueobject;

import java.util.UUID;

public record ProfileId(
        UUID value) {
    public ProfileId {
        if (value == null) {
            throw new IllegalArgumentException("id null");
        }
    }
}
