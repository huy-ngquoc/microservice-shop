package vn.uit.edu.msshop.order.adapter.remote;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import vn.uit.edu.msshop.order.adapter.in.web.request.FindVariantsByIdsRequest;
import vn.uit.edu.msshop.order.adapter.in.web.response.VariantResponse;
import vn.uit.edu.msshop.order.config.FeignConfig;

@FeignClient(name="product-service",configuration=FeignConfig.class)
public interface VariantChecker {
    @PostMapping("/variants/order-search")
    public ResponseEntity<List<VariantResponse>> findAllByIds(
            @RequestBody
            FindVariantsByIdsRequest request);
}
