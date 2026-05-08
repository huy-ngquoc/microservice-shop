package vn.uit.edu.msshop.inventory.adapter.in.web.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vn.uit.edu.msshop.inventory.adapter.in.web.request.common.ChangeRequest;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInventoryRequest {
    private UUID variantId;
    private ChangeRequest<Integer> newQuantity;
    private ChangeRequest<Integer> newReservedQuantity;
    private ChangeRequest<String> newInventoryStatus;
}
