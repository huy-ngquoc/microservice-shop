package vn.uit.edu.payment.application.port.out;

import vn.uit.edu.payment.domain.model.Payment;

public interface SavePaymentPort {
    public Payment save(Payment payment);
}
