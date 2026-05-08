package vn.uit.edu.msshop.inventory.adapter.out.persistence;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

@Repository
public interface SpringDataInventoryJpaRepository extends JpaRepository<InventoryJpaEntity, UUID> {
    public Optional<InventoryJpaEntity> findByVariantId(UUID variantId);
    public Page<InventoryJpaEntity> findAll(Pageable pageable);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public List<InventoryJpaEntity> findByVariantIdInAndStatus(List<UUID> variantIds,String status);
    public List<InventoryJpaEntity> findByVariantIdIn(List<UUID> variantIds);
    public Optional<InventoryJpaEntity> findByVariantIdAndStatus(UUID variantId, String status);

    public Page<InventoryJpaEntity> findByCreateAtBetweenOrLastUpdateBetween(Instant startFirst, Instant endFirst, Instant startSecond, Instant endSecond, Pageable pageable);

    

}
