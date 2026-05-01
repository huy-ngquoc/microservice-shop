package vn.uit.edu.payment.application.port.in;

import vn.uit.edu.payment.application.dto.command.UpdatePaymentCommand;
import vn.uit.edu.payment.application.dto.query.PaymentView;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;

public interface UpdatePaymentUseCase {
    public PaymentView update(UpdatePaymentCommand command);
    public void onlinePaymentExpire(OrderId orderId);
    public void onlinePaymentCancelled(OrderId orderId);
}
