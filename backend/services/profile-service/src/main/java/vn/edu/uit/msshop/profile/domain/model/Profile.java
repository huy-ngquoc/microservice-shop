package vn.edu.uit.msshop.profile.domain.model;

import java.util.Objects;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder(
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

    @Builder
    public static record Draft(
            @NonNull
            ProfileId id,

            @NonNull
            FullName fullName,

            ShippingAddress address,
            PhoneNumber phoneNumber,
            EmailAddress email) {
    }

    @Builder
    public static record UpdateInfo(
            @NonNull
            ProfileId id,

            @NonNull
            FullName fullName,

            ShippingAddress address,
            PhoneNumber phoneNumber,
            EmailAddress email) {
    }

    @Builder
    public static record Snapshot(
            @NonNull
            ProfileId id,

            @NonNull
            FullName fullName,

            ShippingAddress address,
            PhoneNumber phoneNumber,
            EmailAddress email,
            Avatar avatar) {
    }

    @NullMarked
    public static Profile create(
            final Draft d) {
        if (d == null) {
            throw new IllegalArgumentException("Draft must NOT be null");
        }

        if (d.id() == null) {
            throw new IllegalArgumentException("Id must NOT be null");
        }

        if (d.fullName() == null) {
            throw new IllegalArgumentException("Full name must NOT be null");
        }

        return Profile.builder()
                .id(d.id())
                .fullName(d.fullName())
                .address(d.address())
                .phoneNumber(d.phoneNumber())
                .email(d.email())
                .build();
    }

    @NullMarked
    public static Profile reconstitute(
            final Snapshot s) {
        if (s == null) {
            throw new IllegalArgumentException("Snapshot must NOT be null");
        }

        if (s.id() == null) {
            throw new IllegalArgumentException("Id must NOT be null");
        }

        if (s.fullName() == null) {
            throw new IllegalArgumentException("Full name must NOT be null");
        }

        return Profile.builder()
                .id(s.id())
                .fullName(s.fullName())
                .address(s.address())
                .phoneNumber(s.phoneNumber())
                .email(s.email())
                .avatar(s.avatar())
                .build();
    }

    @NullMarked
    public Profile applyUpdateInfo(
            final UpdateInfo u) {
        if (u == null) {
            throw new IllegalArgumentException("Update must NOT be null");
        }

        if (u.fullName() == null) {
            throw new IllegalArgumentException("New full name must NOT be null");
        }

        if (this.isSameInfoWithUpdateInfo(u)) {
            return this;
        }

        return Profile.builder()
                .id(this.id)
                .fullName(u.fullName())
                .address(u.address())
                .phoneNumber(u.phoneNumber())
                .email(u.email())
                .build();
    }

    @NullMarked
    public Profile withAvatar(
            final Avatar newAvatar) {
        if (Objects.equals(newAvatar, this.avatar)) {
            return this;
        }

        return Profile.builder()
                .id(this.id)
                .fullName(this.fullName)
                .address(this.address)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .avatar(newAvatar)
                .build();
    }

    @NullMarked
    public Snapshot snapshot() {
        return Snapshot.builder()
                .id(this.id)
                .fullName(this.fullName)
                .address(this.address)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .avatar(this.avatar)
                .build();
    }

    @NullMarked
    @SuppressWarnings("java:S1067")
    private boolean isSameInfoWithUpdateInfo(
            final UpdateInfo u) {
        return Objects.equals(u.fullName(), this.fullName)
                && Objects.equals(u.email(), this.email)
                && Objects.equals(u.phoneNumber(), this.phoneNumber)
                && Objects.equals(u.address(), this.address);
    }
}
