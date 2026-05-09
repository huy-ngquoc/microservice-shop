package vn.uit.edu.payment.application.port.out;

import java.util.List;

import vn.uit.edu.payment.domain.model.Payment;

public interface SavePaymentPort {
    public Payment save(
            Payment payment);

    public List<Payment> saveAll(
            List<Payment> payments);
}
