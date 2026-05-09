package vn.uit.edu.payment.application.port.in;

import vn.uit.edu.payment.domain.model.Payment;

public interface CreateOnlinePaymentInfoUseCase {
    public String createPaymentLink(
            Payment payment);
}
