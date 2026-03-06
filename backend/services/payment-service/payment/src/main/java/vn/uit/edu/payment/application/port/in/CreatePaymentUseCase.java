package vn.uit.edu.payment.application.port.in;

import vn.uit.edu.payment.application.dto.command.CreatePaymentCommand;

public interface CreatePaymentUseCase {
    public void create(CreatePaymentCommand command);
}
