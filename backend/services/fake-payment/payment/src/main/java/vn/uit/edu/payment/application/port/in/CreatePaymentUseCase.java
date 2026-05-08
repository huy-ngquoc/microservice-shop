package vn.uit.edu.payment.application.port.in;

import vn.uit.edu.payment.application.dto.command.CreatePaymentCommand;
import vn.uit.edu.payment.application.dto.query.PaymentView;

public interface CreatePaymentUseCase {
    public PaymentView create(CreatePaymentCommand command);
}
