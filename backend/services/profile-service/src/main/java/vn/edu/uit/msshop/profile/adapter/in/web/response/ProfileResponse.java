package vn.edu.uit.msshop.profile.adapter.in.web.response;

import org.jspecify.annotations.Nullable;

public record ProfileResponse(
        String id,

        String fullName,

        @Nullable
        String email,

        @Nullable
        String phoneNumber,

        @Nullable
        String address,

        @Nullable
        String avatarUrl) {
}
