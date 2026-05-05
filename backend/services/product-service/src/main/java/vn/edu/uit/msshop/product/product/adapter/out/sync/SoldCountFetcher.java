package vn.edu.uit.msshop.product.product.adapter.out.sync;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import vn.edu.uit.msshop.product.product.adapter.in.web.response.VariantSoldCountResponse;

@FeignClient(name = "order-service")
public interface SoldCountFetcher {
  @GetMapping("/order/public/sold_counts")
  public List<VariantSoldCountResponse> getSoldCounts();
}
