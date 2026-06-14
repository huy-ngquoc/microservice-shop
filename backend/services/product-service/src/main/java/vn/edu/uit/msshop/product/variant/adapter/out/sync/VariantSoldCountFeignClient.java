package vn.edu.uit.msshop.product.variant.adapter.out.sync;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import vn.edu.uit.msshop.product.variant.adapter.out.sync.response.VariantSoldCountResponse;

@FeignClient(
        name = "order-service")
public interface VariantSoldCountFeignClient {
    @GetMapping("/order/public/sold_counts")
    List<VariantSoldCountResponse> getSoldCounts();
}
