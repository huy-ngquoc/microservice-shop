package vn.uit.edu.msshop.inventory.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.inventory.adapter.out.persistence.InventoryJpaEntity;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.LastUpdate;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.ReservedQuantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Version;

@Component
public class InventoryJpaMapper {
    public InventoryJpaEntity toEntity(Inventory domain) {
        return new InventoryJpaEntity(domain.getId().value(), domain.getVariantId().value(), domain.getQuantity().value(), domain.getReservedQuantity().value(), domain.getLastUpdate().value(),domain.getStatus().value(), domain.getVersion().value());
    }
    public Inventory toDomain(InventoryJpaEntity entity) {
        final var snapshot = Inventory.Snapshot.builder().id(new InventoryId(entity.getId()))
        .variantId(new VariantId(entity.getVariantId()))
        .quantity(new Quantity(entity.getQuantity()))
        .reservedQuantity(new ReservedQuantity(entity.getReservedQuantity()))
        .lastUpdate(new LastUpdate(entity.getLastUpdate()))
        .version(new Version(entity.getVersion()))
        .build();
        return Inventory.reconstitue(snapshot);
    }
    public InventoryJpaEntity toNew(VariantId variantId) {
        return InventoryJpaEntity.of(variantId.value(), 0, 0, null, "ENABLE");
    }
    public InventoryJpaEntity toNewFromCommand(VariantId variantId, Quantity quantity) {
        return InventoryJpaEntity.of(variantId.value(), quantity.value(), 0, null,"ENABLE");
    }
}
