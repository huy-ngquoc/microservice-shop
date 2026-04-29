package vn.uit.edu.payment.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.adapter.out.persistence.SpringDataPaymentJpaRepository;

@RestController
@RequiredArgsConstructor
public class PaymentTestController {
    private final SpringDataPaymentJpaRepository repo;
    @GetMapping("/test")
    public ResponseEntity<PaymentTestResponse> getResponse() {
        long success=repo.countByStatus("SUCCESS");
        long pending=repo.countByStatus("PENDING");
        long expired=repo.countByStatus("EXPIRED");
        return ResponseEntity.ok(new PaymentTestResponse(pending, expired, success));
    }
}
