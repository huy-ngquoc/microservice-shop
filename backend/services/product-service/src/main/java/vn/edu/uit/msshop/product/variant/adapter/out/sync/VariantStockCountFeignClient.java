package vn.edu.uit.msshop.product.variant.adapter.out.sync;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.edu.uit.msshop.product.variant.adapter.out.sync.request.VariantStockCountBulkFetchRequest;
import vn.edu.uit.msshop.product.variant.adapter.out.sync.response.VariantStockCountResponse;

@FeignClient(
        name = "inventory-service")
public interface VariantStockCountFeignClient {

    @PostMapping("/inventory/public/updated_inventory")
    PageResponseDto<VariantStockCountResponse> getStockCounts(
            @RequestBody
            VariantStockCountBulkFetchRequest request,

            @RequestParam
            int pageNumber,

            @RequestParam
            int pageSize);
}
