package vn.edu.uit.msshop.profile.adapter.out.persistence;

import java.util.UUID;

import org.jspecify.annotations.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "profiles")
@Getter
@Setter
@NoArgsConstructor(
        access = AccessLevel.PACKAGE)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class ProfileJpaEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(
            nullable = false,
            length = 200)
    private String fullName;

    @Column(
            length = 500)
    private String address;

    @Column(
            length = 30)
    private String phoneNumber;

    @Column(
            length = 255)
    private String email;

    // --- Avatar (flatten from value-object Avatar) ---

    @Column(
            length = 2048)
    private String avatarUrl;

    @Column(
            length = 255)
    private String avatarPublicId;

    private Integer avatarWidth;

    private Integer avatarHeight;

    @Version
    @Column(
            nullable = false)
    private long version;

    public static @NonNull ProfileJpaEntity of(
            UUID id,
            String fullName,
            String email,
            String phoneNumber,
            String address,
            String avatarUrl,
            String avatarPublicId,
            Integer avatarWidth,
            Integer avatarHeight) {
        return new ProfileJpaEntity(
                id,
                fullName,
                address,
                phoneNumber,
                email,
                avatarUrl,
                avatarPublicId,
                avatarWidth,
                avatarHeight,
                0L);
    }
}
