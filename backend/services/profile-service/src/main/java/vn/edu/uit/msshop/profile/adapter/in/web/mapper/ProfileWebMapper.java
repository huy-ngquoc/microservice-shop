package vn.edu.uit.msshop.profile.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.profile.adapter.in.web.request.CreateProfileRequest;
import vn.edu.uit.msshop.profile.adapter.in.web.request.UpdateProfileInfoRequest;
import vn.edu.uit.msshop.profile.adapter.in.web.request.common.ChangeRequest;
import vn.edu.uit.msshop.profile.adapter.in.web.request.common.PatchRequest;
import vn.edu.uit.msshop.profile.adapter.in.web.response.ProfileResponse;
import vn.edu.uit.msshop.profile.application.dto.command.CreateProfileCommand;
import vn.edu.uit.msshop.profile.application.dto.command.UpdateProfileCommand;
import vn.edu.uit.msshop.profile.application.dto.query.ProfileView;
import vn.edu.uit.msshop.profile.domain.model.valueobject.EmailAddress;
import vn.edu.uit.msshop.profile.domain.model.valueobject.FullName;
import vn.edu.uit.msshop.profile.domain.model.valueobject.PhoneNumber;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ShippingAddress;

@Component
public class ProfileWebMapper {
    public CreateProfileCommand toCommand(
            final CreateProfileRequest req) {
        final var id = new ProfileId(req.id());
        final var fullName = new FullName(req.fullName());
        final var email = EmailAddress.fromValueOrNull(req.email());
        final var phoneNumber = PhoneNumber.fromValueOrNull(req.phoneNumber());
        final var address = ShippingAddress.fromValueOrNull(req.address());

        return new CreateProfileCommand(
                id,
                fullName,
                email,
                phoneNumber,
                address);
    }

    public UpdateProfileCommand toCommand(
            final UpdateProfileInfoRequest req) {
        final var id = new ProfileId(req.id());

        final var fullName = ChangeRequest.toChange(req.fullName(), FullName::new);

        final var email = PatchRequest.toPatch(req.email(), EmailAddress::new);
        final var phoneNumber = PatchRequest.toPatch(req.phoneNumber(), PhoneNumber::new);
        final var address = PatchRequest.toPatch(req.address(), ShippingAddress::new);

        return new UpdateProfileCommand(
                id,
                fullName,
                email,
                phoneNumber,
                address);
    }

    public ProfileResponse toResponse(
            final ProfileView view) {
        return new ProfileResponse(
                view.id(),
                view.fullName(),
                view.email(),
                view.phoneNumber(),
                view.address(),
                view.avatarUrl());
    }
}
