package vn.uit.edu.msshop.inventory.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.inventory.adapter.out.persistence.InventoryJpaEntity;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.LastUpdate;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.ReservedQuantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Component
public class InventoryJpaMapper {
    public InventoryJpaEntity toEntity(Inventory domain) {
        return new InventoryJpaEntity(domain.getId().value(), domain.getVariantId().value(), domain.getQuantity().value(), domain.getReservedQuantity().value(), domain.getLastUpdate().value(), 0L);
    }
    public Inventory toDomain(InventoryJpaEntity entity) {
        final var snapshot = Inventory.Snapshot.builder().id(new InventoryId(entity.getId()))
        .variantId(new VariantId(entity.getVariantId()))
        .quantity(new Quantity(entity.getQuantity()))
        .reservedQuantity(new ReservedQuantity(entity.getReservedQuantity()))
        .lastUpdate(new LastUpdate(entity.getLastUpdate())).build();
        return Inventory.reconstitue(snapshot);
    }
}
