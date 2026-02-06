package vn.edu.uit.msshop.profile.domain.model;

import java.util.Objects;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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

    private Profile(
            @NonNull
            final ProfileId id,

            @NonNull
            final FullName fullName,

            final ShippingAddress address,
            final PhoneNumber phoneNumber,
            final EmailAddress email,
            final Avatar avatar) {
        this.id = Objects.requireNonNull(id);
        this.fullName = Objects.requireNonNull(fullName);
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.avatar = avatar;
    }

    public static @NonNull Profile create(
            @NonNull
            final ProfileId id,

            @NonNull
            final FullName fullName,

            final EmailAddress email) {
        return new Profile(
                id,
                fullName,
                null,
                null,
                email,
                null);
    }

    public @NonNull Profile withFullName(
            @NonNull
            final FullName newName) {
        return new Profile(
                id,
                Objects.requireNonNull(newName),
                address,
                phoneNumber,
                email,
                avatar);
    }

    public @NonNull Profile withAddress(
            @Nullable
            final ShippingAddress newAddress) {
        return new Profile(
                id,
                fullName,
                newAddress,
                phoneNumber,
                email,
                avatar);
    }

    public @NonNull Profile withEmail(
            @Nullable
            final EmailAddress newEmail) {
        return new Profile(
                id,
                fullName,
                address,
                phoneNumber,
                newEmail,
                avatar);
    }

    public @NonNull Profile withAvatar(
            @Nullable
            final Avatar newAvatar) {
        return new Profile(
                id,
                fullName,
                address,
                phoneNumber,
                email,
                newAvatar);
    }

    public @NonNull Profile withoutAvatar() {
        return new Profile(
                id,
                fullName,
                address,
                phoneNumber,
                email,
                null);
    }
}
