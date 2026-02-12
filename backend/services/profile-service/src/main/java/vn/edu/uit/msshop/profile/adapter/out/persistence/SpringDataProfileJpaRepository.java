package vn.edu.uit.msshop.profile.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProfileJpaRepository
        extends JpaRepository<ProfileJpaEntity, UUID> {
    Optional<ProfileJpaEntity> findByEmail(
            final String email);

    boolean existsByEmail(
            final String email);
}
