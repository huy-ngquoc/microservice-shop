package vn.edu.uit.msshop.product.variant.adapter.out.sync;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import vn.edu.uit.msshop.product.variant.adapter.out.sync.response.StockCountResponse;

@FeignClient(
        name = "inventory-service")
public interface InventoryStockCountFeignClient {
    @GetMapping("/inventory/public/stock_counts")
    List<StockCountResponse> getStockCounts();
}
