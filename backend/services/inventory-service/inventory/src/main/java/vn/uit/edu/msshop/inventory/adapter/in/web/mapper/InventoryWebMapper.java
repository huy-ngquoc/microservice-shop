package vn.uit.edu.msshop.inventory.adapter.in.web.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.inventory.adapter.in.web.request.CreateInventoryRequest;
import vn.uit.edu.msshop.inventory.adapter.in.web.request.UpdateInventoryFromOrderServiceRequest;
import vn.uit.edu.msshop.inventory.adapter.in.web.request.UpdateInventoryRequest;
import vn.uit.edu.msshop.inventory.adapter.in.web.request.common.ChangeRequest;
import vn.uit.edu.msshop.inventory.adapter.in.web.response.InventoryResponse;
import vn.uit.edu.msshop.inventory.application.dto.command.CreateInventoryCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderCancelledCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderCreateCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderDetailCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderShippedCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.UpdateInventoryCommand;
import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;
import vn.uit.edu.msshop.inventory.domain.event.OrderCreated;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.OrderQuantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.OrderStatus;
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
        return new InventoryResponse(view.getId(),view.getVariantId(),view.getQuantity(),view.getReservedQuantity(),view.getLastUpdate(), view.getStatus());
    }
    public OrderCreateCommand toCommand(OrderCreated event) {
        List<OrderDetailCommand> detailCommands = event.getDetails().stream().map(item->new OrderDetailCommand(new VariantId(item.getVariantId()), new OrderQuantity(item.getAmount()))).toList();
        return new OrderCreateCommand(detailCommands,event.getOrderId());
    }
   
    public CreateInventoryCommand toCommand(CreateInventoryRequest request) {
        return new CreateInventoryCommand(new VariantId(request.getVariantId()),new Quantity(request.getQuantity()));
    }
    
    public OrderShippedCommand toOrderShippedCommand(UpdateInventoryFromOrderServiceRequest request) {
        List<OrderDetailCommand> detailCommands = request.getDetailRequests().stream().map(item->new OrderDetailCommand(new VariantId(item.getVariantId()), new OrderQuantity(item.getQuantity()))).toList();
        return new OrderShippedCommand(detailCommands,request.getOrderId());
    }
    public OrderCancelledCommand toOrderCancelledCommand(UpdateInventoryFromOrderServiceRequest  request ) {
        List<OrderDetailCommand> detailCommands = request.getDetailRequests().stream().map(item->new OrderDetailCommand(new VariantId(item.getVariantId()), new OrderQuantity(item.getQuantity()))).toList();
        return new OrderCancelledCommand(detailCommands,request.getOrderId(), new OrderStatus(request.getOldStatus()));
    }
    
    
}
