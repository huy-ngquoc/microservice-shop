package vn.uit.edu.msshop.inventory.domain.model;

import java.time.Instant;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.uit.edu.msshop.inventory.application.exception.InsufficientStockException;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryStatus;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.LastUpdate;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.ReservedQuantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Version;

@Getter

@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@Builder(
        access = AccessLevel.PRIVATE)
public class Inventory  {
    private InventoryId id;
    private VariantId variantId;
    private Quantity quantity;
    private ReservedQuantity reservedQuantity;
    private LastUpdate lastUpdate;
    private Version version;
    private InventoryStatus status;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventory i = (Inventory) o;
        return Objects.equals(this.variantId.value(), i.variantId.value());
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.variantId.value());
    }


    

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
        LastUpdate lastUpdate,
        InventoryStatus status,
        Version version
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
        return Inventory.builder().id(draft.id()).variantId(draft.variantId()).quantity(draft.quantity()).reservedQuantity(draft.reservedQuantity()).lastUpdate(new LastUpdate(null)).status(new InventoryStatus("ENABLE")).version(new Version(0)).build();
    }

    public static Inventory reconstitue(Snapshot s) {
        if(s==null) throw new IllegalArgumentException("Invalid snapshot");
        return Inventory.builder().id(s.id()).variantId(s.variantId()).quantity(s.quantity()).reservedQuantity(s.reservedQuantity()).lastUpdate(new LastUpdate(null)).version(s.version()).status(s.status()).build();
    }

    public Snapshot snapshot() {
        return Snapshot.builder().id(this.id).variantId(this.variantId).quantity(this.quantity).reservedQuantity(this.reservedQuantity).lastUpdate(this.lastUpdate).status(this.status).build();
    }
    public Inventory applyUpdateInfo(UpdateInfo u) {
        if(u==null) throw new IllegalArgumentException("Update info must not be null");
        if(isTheSameInfoWithUpdate(u)) return this;
        return Inventory.builder().id(this.id).variantId(this.variantId).quantity(u.quantity()).reservedQuantity(u.reservedQuantity()).lastUpdate(new LastUpdate(Instant.now())).version(this.version).status(this.status).build();
    }
    private boolean isTheSameInfoWithUpdate(UpdateInfo u) {
        return u.quantity().value()==this.quantity.value()&&u.reservedQuantity().value()==this.reservedQuantity.value();
    }
    public void reserveStock(int amount) {
    if (this.quantity.value() < amount) {
        throw new InsufficientStockException(this.variantId);
    }
    this.quantity = new Quantity(this.quantity.value() - amount);
    this.reservedQuantity = new ReservedQuantity(this.reservedQuantity.value() + amount);
}

public void releaseReservedStock(int amount) {
    this.reservedQuantity = new ReservedQuantity(this.reservedQuantity.value() - amount);
    this.quantity = new Quantity(this.quantity.value() + amount);
}
public Inventory disable() {
    return Inventory.builder().id(this.id).variantId(this.variantId).quantity(this.quantity).reservedQuantity(this.reservedQuantity).lastUpdate(new LastUpdate(Instant.now())).version(this.version).status(new InventoryStatus("DISABLE")).build();
}
public Inventory restore() {
    return Inventory.builder().id(this.id).variantId(this.variantId).quantity(this.quantity).reservedQuantity(this.reservedQuantity).lastUpdate(new LastUpdate(Instant.now())).version(this.version).status(new InventoryStatus("ENABLE")).build();

}
}
