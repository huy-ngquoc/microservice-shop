package vn.uit.edu.msshop.order.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.out.persistence.OrderRepository;

@RestController
@RequiredArgsConstructor
public class PaymentTestController {
    private final OrderRepository orderRepo;
    @GetMapping("/statistic")
    public ResponseEntity<TestResponse> getStatisticData() {
        long confirmedPaid=orderRepo.countByStatusAndPaymentStatus("CONFIRMED", "SUCCESS");
        long confirmedUnpaid=orderRepo.countByStatusAndPaymentStatus("CONFIRMED","PENDING");
        long error=orderRepo.countByStatus("PAYMENT_ERROR");
        long paymentExpired=orderRepo.countByStatus("PAYMENT_EXPIRED");
        return ResponseEntity.ok(new TestResponse(confirmedPaid, confirmedUnpaid, error, paymentExpired));
    }
}
