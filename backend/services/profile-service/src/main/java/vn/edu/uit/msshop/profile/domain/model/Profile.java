package vn.edu.uit.msshop.profile.domain.model;

import java.util.Objects;

import org.jspecify.annotations.NonNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.profile.domain.model.valueobject.Avatar;
import vn.edu.uit.msshop.profile.domain.model.valueobject.EmailAddress;
import vn.edu.uit.msshop.profile.domain.model.valueobject.FullName;
import vn.edu.uit.msshop.profile.domain.model.valueobject.PhoneNumber;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ShippingAddress;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
public final class Profile {
    @EqualsAndHashCode.Include
    @NonNull
    private final ProfileId id;

    @NonNull
    private final FullName fullName;

    private final ShippingAddress address;

    private final PhoneNumber phoneNumber;

    private final EmailAddress email;

    private final Avatar avatar;

    public static @NonNull Profile create(
            @NonNull
            final ProfileId id,

            @NonNull
            final FullName fullName,

            final EmailAddress email) {
        if (fullName == null) {
            throw new IllegalArgumentException("Full name must NOT be null");
        }

        return new Profile(
                id,
                fullName,
                null,
                null,
                email,
                null);
    }

    public @NonNull Profile with(
            @NonNull
            final FullName newFullName,

            final EmailAddress newEmail,
            final PhoneNumber newPhoneNumber,
            final ShippingAddress newAddress,
            final Avatar newAvatar) {
        if (newFullName == null) {
            throw new IllegalArgumentException("New full name must NOT be null");
        }

        if (isSameInfo(newFullName, newEmail, newPhoneNumber, newAddress, newAvatar)) {
            return this;
        }

        return new Profile(
                this.id,
                newFullName,
                newAddress,
                newPhoneNumber,
                newEmail,
                newAvatar);
    }

    @SuppressWarnings("java:S1067")
    private boolean isSameInfo(
            final FullName newFullName,
            final EmailAddress newEmail,
            final PhoneNumber newPhoneNumber,
            final ShippingAddress newAddress,
            final Avatar newAvatar) {
        return Objects.equals(newFullName, this.fullName)
                && Objects.equals(newEmail, this.email)
                && Objects.equals(newPhoneNumber, this.phoneNumber)
                && Objects.equals(newAddress, this.address)
                && Objects.equals(newAvatar, this.avatar);
    }
}
