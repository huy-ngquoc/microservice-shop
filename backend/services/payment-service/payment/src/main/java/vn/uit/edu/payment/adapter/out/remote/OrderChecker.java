package vn.uit.edu.payment.adapter.out.remote;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vn.uit.edu.payment.adapter.in.web.response.OrderResponse;
@FeignClient(name="order-service")
public interface OrderChecker {
    @GetMapping("/order/public/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable UUID id);
}
