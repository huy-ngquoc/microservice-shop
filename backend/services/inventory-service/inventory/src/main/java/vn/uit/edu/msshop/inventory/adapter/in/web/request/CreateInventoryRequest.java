package vn.uit.edu.msshop.inventory.adapter.in.web.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateInventoryRequest {
    private UUID variantId;
    private int quantity;
}
