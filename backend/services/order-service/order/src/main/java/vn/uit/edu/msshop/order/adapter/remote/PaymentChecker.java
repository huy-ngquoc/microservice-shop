package vn.uit.edu.msshop.order.adapter.remote;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vn.uit.edu.msshop.order.adapter.in.web.response.PaymentResponse;


@FeignClient(name="payment-service")
public interface PaymentChecker {
    @GetMapping("/payment/public/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable UUID orderId);
}
