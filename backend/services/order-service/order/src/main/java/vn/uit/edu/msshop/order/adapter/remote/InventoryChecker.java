package vn.uit.edu.msshop.order.adapter.remote;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import vn.uit.edu.msshop.order.adapter.in.web.request.CreateInventoryRequest;
import vn.uit.edu.msshop.order.adapter.in.web.request.OrderDetailRequest;
import vn.uit.edu.msshop.order.adapter.in.web.response.InventoryResponse;

@FeignClient(name="inventory-service")
public interface InventoryChecker {
    @PostMapping("/inventory/public/variants")
    List<InventoryResponse> getInventoryBatch(
        @RequestBody List<UUID> variantIds
    );
    @PostMapping("/inventory/public/process_order")
    void processOrder(@RequestBody List<OrderDetailRequest> requests);
    @PostMapping("/inventory/public/create") 
    public ResponseEntity<Void> create( @RequestBody List<CreateInventoryRequest> requests);
}
