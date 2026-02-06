package vn.edu.uit.msshop.profile.domain.model.valueobject;

import java.util.UUID;

import org.jspecify.annotations.NonNull;

public record ProfileId(
        @NonNull
        UUID value) {
    public ProfileId {
        if (value == null) {
            throw new IllegalArgumentException("id null");
        }
    }
}
