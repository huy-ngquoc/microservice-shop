package vn.edu.uit.msshop.profile.application.dto.command;

import org.jspecify.annotations.NullMarked;

import vn.edu.uit.msshop.profile.application.common.Change;
import vn.edu.uit.msshop.profile.application.common.Patch;
import vn.edu.uit.msshop.profile.domain.model.valueobject.EmailAddress;
import vn.edu.uit.msshop.profile.domain.model.valueobject.FullName;
import vn.edu.uit.msshop.profile.domain.model.valueobject.PhoneNumber;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ShippingAddress;

@NullMarked
public record UpdateProfileCommand(
        ProfileId profileId,

        Change<FullName> fullName,

        Patch<EmailAddress> email,

        Patch<PhoneNumber> phoneNumber,

        Patch<ShippingAddress> address) {
}
