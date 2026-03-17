package vn.uit.edu.msshop.inventory.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.inventory.adapter.in.web.request.UpdateInventoryRequest;
import vn.uit.edu.msshop.inventory.adapter.in.web.request.common.ChangeRequest;
import vn.uit.edu.msshop.inventory.adapter.in.web.response.InventoryResponse;
import vn.uit.edu.msshop.inventory.application.dto.command.UpdateInventoryCommand;
import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.ReservedQuantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Component
public class InventoryWebMapper {
    public UpdateInventoryCommand toCommand(UpdateInventoryRequest request) {
        final var variantId = new VariantId(request.getVariantId());
        final var quantity = ChangeRequest.toChange(request.getNewQuantity(), Quantity::new);
        final var reservedQuantity=ChangeRequest.toChange(request.getNewReservedQuantity(), ReservedQuantity::new);
        return new UpdateInventoryCommand(variantId, quantity, reservedQuantity);
    }
    public InventoryResponse toResponse(InventoryView view) {
        return new InventoryResponse(view.getId(),view.getVariantId(),view.getQuantity(),view.getReservedQuantity(),view.getLastUpdate());
    }
}
