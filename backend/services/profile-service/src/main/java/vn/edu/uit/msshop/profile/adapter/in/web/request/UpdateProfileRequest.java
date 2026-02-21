package vn.edu.uit.msshop.profile.adapter.in.web.request;

import java.util.UUID;

import org.jspecify.annotations.NullUnmarked;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.profile.adapter.in.web.request.common.ChangeRequest;
import vn.edu.uit.msshop.profile.adapter.in.web.request.common.PatchRequest;

@NullUnmarked
public record UpdateProfileRequest(
        @NotNull
        UUID id,

        @Valid
        ChangeRequest<@Size(
                max = 200) String> fullName,

        @Valid
        PatchRequest<@Email @Size(
                max = 255) String> email,

        @Valid
        PatchRequest<@Size(
                max = 30) String> phoneNumber,

        @Valid
        PatchRequest<@Size(
                max = 30) String> address) {
}
