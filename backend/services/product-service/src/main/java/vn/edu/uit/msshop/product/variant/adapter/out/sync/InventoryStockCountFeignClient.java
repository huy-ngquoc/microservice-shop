package vn.edu.uit.msshop.product.variant.adapter.out.sync;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import vn.edu.uit.msshop.product.variant.adapter.out.sync.response.StockCountResponse;

// @PostMapping("/public/updated_inventory")
//     public ResponseEntity<Page<InventoryResponse>> getUpdatedInventory(@RequestBody GetUpdatedInventoryRequest request, @RequestParam(defaultValue="0") int pageNumber, @RequestParam(defaultValue="7") int pageSize) {
//         Pageable pageable = PageRequest.of(pageNumber, pageSize);
//         final var result = findUseCase.findAllUpdatedInventory(request.getStartFirst(), request.getEndFirst(), request.getStartSecond(), request.getEndSecond(), pageable);
//         return ResponseEntity.ok(result.map(mapper::toResponse));
//     }

@FeignClient(
        name = "inventory-service")
public interface InventoryStockCountFeignClient {
    @GetMapping("/inventory/public/stock_counts")
    List<StockCountResponse> getStockCounts();
}
