package vn.uit.edu.msshop.inventory.adapter.out.persistence;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Inventory")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryJpaEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(unique=true)
    private UUID variantId;
    private int quantity;
    private int reservedQuantity;
    private Instant lastUpdate;
    private String status;

    @Version
    @Column(
            nullable = false)
    private long version;

    public static InventoryJpaEntity of(UUID variantId, int quantity, int reservedQuantity, Instant lastUpdate, String status) {
        return InventoryJpaEntity.builder().variantId(variantId).quantity(quantity).reservedQuantity(reservedQuantity).lastUpdate(lastUpdate).version(0L).status(status).build();
    }

}
