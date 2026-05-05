package vn.edu.uit.msshop.product.variant.adapter.out.sync;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.edu.uit.msshop.product.variant.adapter.out.sync.request.FindAllUpdatedStockCountsRequest;
import vn.edu.uit.msshop.product.variant.adapter.out.sync.response.StockCountResponse;

@FeignClient(name = "inventory-service")
public interface InventoryStockCountFeignClient {
  @PostMapping("/inventory/public/updated_inventory")
  PageResponseDto<StockCountResponse> getStockCounts(
      @RequestBody FindAllUpdatedStockCountsRequest request,

      @RequestParam int pageNumber,

      @RequestParam int pageSize);
}
