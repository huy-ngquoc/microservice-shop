package vn.uit.edu.payment.application.port.in;

import vn.uit.edu.payment.application.dto.command.UpdatePaymentCommand;

public interface UpdatePaymentUseCase {
    public void update(UpdatePaymentCommand command);
}
