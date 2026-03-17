package vn.uit.edu.msshop.inventory.domain.model;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.LastUpdate;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.ReservedQuantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@Builder(
        access = AccessLevel.PRIVATE)
public class Inventory {
    private InventoryId id;
    private VariantId variantId;
    private Quantity quantity;
    private ReservedQuantity reservedQuantity;
    private LastUpdate lastUpdate;

    @Builder
    public static record Draft (
        InventoryId id,
        VariantId variantId,
        Quantity quantity,
        ReservedQuantity reservedQuantity

    ) {

    }
    @Builder
    public static record Snapshot (
        InventoryId id,
        VariantId variantId,
        Quantity quantity,
        ReservedQuantity reservedQuantity,
        LastUpdate lastUpdate
    ) {

    }

    @Builder
    public static record UpdateInfo (
        InventoryId inventoryId,
        Quantity quantity,
        ReservedQuantity reservedQuantity
    )  {}

    public static Inventory create(Draft draft) {
        if(draft==null) throw new IllegalArgumentException("Invalid draft");
        return Inventory.builder().id(draft.id()).variantId(draft.variantId()).quantity(draft.quantity()).reservedQuantity(draft.reservedQuantity()).lastUpdate(new LastUpdate(null)).build();
    }

    public static Inventory reconstitue(Snapshot s) {
        if(s==null) throw new IllegalArgumentException("Invalid snapshot");
        return Inventory.builder().id(s.id()).variantId(s.variantId()).quantity(s.quantity()).reservedQuantity(s.reservedQuantity()).lastUpdate(new LastUpdate(null)).build();
    }

    public Snapshot snapshot() {
        return Snapshot.builder().id(this.id).variantId(this.variantId).quantity(this.quantity).reservedQuantity(this.reservedQuantity).lastUpdate(this.lastUpdate).build();
    }
    public Inventory applyUpdateInfo(UpdateInfo u) {
        if(u==null) throw new IllegalArgumentException("Update info must not be null");
        if(isTheSameInfoWithUpdate(u)) return this;
        return Inventory.builder().id(this.id).variantId(this.variantId).quantity(u.quantity()).reservedQuantity(u.reservedQuantity()).lastUpdate(new LastUpdate(Instant.now())).build();
    }
    private boolean isTheSameInfoWithUpdate(UpdateInfo u) {
        return u.quantity().value()==this.quantity.value()&&u.reservedQuantity().value()==this.reservedQuantity.value();
    }
}
