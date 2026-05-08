package vn.uit.edu.msshop.cart.adapter.remote;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import vn.uit.edu.msshop.cart.adapter.in.web.response.InventoryResponse;

@FeignClient(name="inventory-service")
public interface LoadInventoryService {
    @PostMapping("/inventory/public/variants")
    List<InventoryResponse> getInventoryBatch(
        @RequestBody List<UUID> variantIds
    );
}
