package vn.uit.edu.msshop.inventory.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataInventoryJpaRepository extends JpaRepository<InventoryJpaEntity, UUID> {
    public Optional<InventoryJpaEntity> findByVariantId(UUID variantId);
    public Page<InventoryJpaEntity> findAll(Pageable pageable);

}
