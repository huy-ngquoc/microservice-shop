package vn.edu.uit.msshop.profile.application.dto.command;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.profile.domain.model.valueobject.EmailAddress;
import vn.edu.uit.msshop.profile.domain.model.valueobject.FullName;
import vn.edu.uit.msshop.profile.domain.model.valueobject.PhoneNumber;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ShippingAddress;

public record CreateProfileCommand(
        ProfileId profileId,

        FullName fullName,

        @Nullable
        EmailAddress email,

        @Nullable
        PhoneNumber phoneNumber,

        @Nullable
        ShippingAddress address) {
}
