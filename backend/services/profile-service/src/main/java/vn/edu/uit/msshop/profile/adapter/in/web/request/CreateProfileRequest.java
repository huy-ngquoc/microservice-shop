package vn.edu.uit.msshop.profile.adapter.in.web.request;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProfileRequest(
        @NotNull
        UUID id,

        @NotBlank
        @Size(
                max = 200)
        String fullName,

        @Nullable
        @Email
        @Size(
                max = 255)
        String email,

        @Nullable
        @Size(
                max = 30)
        String phoneNumber,

        @Nullable
        @Size(
                max = 500)
        String address) {

}
