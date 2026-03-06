package vn.uit.edu.payment.application.port.in;

import vn.uit.edu.payment.application.dto.command.UpdatePaymentCommand;
import vn.uit.edu.payment.application.dto.query.PaymentView;

public interface UpdatePaymentUseCase {
    public PaymentView update(UpdatePaymentCommand command);
}
