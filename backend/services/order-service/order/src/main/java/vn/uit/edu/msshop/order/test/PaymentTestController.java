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
        long confirmed=orderRepo.countByStatusAndPaymentMethod("CONFIRMED","ONLINE");
        long paymentExpired=orderRepo.countByStatus("PAYMENT_EXPIRED");
        long paymentError=orderRepo.countByStatus("PAYMENT_ERROR");
        long waitingPayment=orderRepo.countByStatus("WAITING_PAYMENT");
        long pendingPayment=orderRepo.countByStatus("PENDING_PAYMENT");
        return ResponseEntity.ok(new TestResponse(confirmed, paymentExpired, paymentError, waitingPayment, pendingPayment));
    }
}
