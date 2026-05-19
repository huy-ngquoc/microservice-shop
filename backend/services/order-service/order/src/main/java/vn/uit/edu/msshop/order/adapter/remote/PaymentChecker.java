package vn.uit.edu.msshop.order.adapter.remote;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import vn.uit.edu.msshop.order.adapter.in.web.response.PaymentResponse;
import vn.uit.edu.msshop.order.bootstrap.config.PaymentFeignConfig;
import vn.uit.edu.msshop.order.domain.event.OrderCreated;

@FeignClient(
        name = "payment-service",
        configuration = PaymentFeignConfig.class)
public interface PaymentChecker {
    @GetMapping("/payment/public/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(
            @PathVariable
            UUID orderId);

    @PostMapping("/payment/public/create_payment")
    public ResponseEntity<Void> createPayment(
            @RequestBody
            OrderCreated orderCreated);
}
