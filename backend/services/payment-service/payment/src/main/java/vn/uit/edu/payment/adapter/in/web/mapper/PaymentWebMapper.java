package vn.uit.edu.payment.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.uit.edu.payment.adapter.in.web.request.CreatePaymentRequest;
import vn.uit.edu.payment.adapter.in.web.request.UpdatePaymentRequest;
import vn.uit.edu.payment.adapter.in.web.request.common.ChangeRequest;
import vn.uit.edu.payment.adapter.in.web.response.PaymentResponse;
import vn.uit.edu.payment.application.dto.command.CreatePaymentCommand;
import vn.uit.edu.payment.application.dto.command.UpdatePaymentCommand;
import vn.uit.edu.payment.application.dto.query.PaymentView;
import vn.uit.edu.payment.domain.event.OrderCreated;
import vn.uit.edu.payment.domain.model.valueobject.Currency;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentMethod;
import vn.uit.edu.payment.domain.model.valueobject.PaymentStatus;
import vn.uit.edu.payment.domain.model.valueobject.PaymentValue;
import vn.uit.edu.payment.domain.model.valueobject.UserId;
/*PaymentId paymentId,
    Currency currency,
    OrderId orderId,
    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus,
    PaymentValue paymentValue */
@Component
public class PaymentWebMapper {
    public CreatePaymentCommand toCommand(CreatePaymentRequest request) {
        return new CreatePaymentCommand(
            new PaymentId(UUID.randomUUID()),
            new Currency(request.currency()),
            new OrderId(request.orderId()),
            new PaymentMethod(request.paymentMethod()),
            new PaymentStatus("PENDING"),
            new PaymentValue(request.paymentValue()),
            new UserId(request.userId())
        );
    }

    public CreatePaymentCommand toCommand(OrderCreated event) {
        return new CreatePaymentCommand(
            new PaymentId(UUID.randomUUID()),
            new Currency(event.currency()),
            new OrderId(event.orderId()),
            new PaymentMethod(event.paymentMethod()),
            new PaymentStatus("PENDING"),
            new PaymentValue(event.paymentValue()),
        new UserId(event.userId())
        );
    }
    
    public UpdatePaymentCommand toCommand(UpdatePaymentRequest request) {
        final var paymentId = new PaymentId(request.paymentId());
        final var currency = ChangeRequest.toChange(request.currency(), Currency::new);
        final var paymentMethod = ChangeRequest.toChange(request.paymentMethod(),PaymentMethod::new);
        final var paymentStatus = ChangeRequest.toChange(request.paymentStatus(), PaymentStatus::new);
        return new UpdatePaymentCommand(paymentId, currency, paymentStatus, paymentMethod);
    }

    public PaymentResponse toResponse(PaymentView paymentView) {
        return new PaymentResponse(paymentView.paymentId(), paymentView.createAt(), paymentView.currency(), paymentView.orderId(), paymentView.paymentMethod(), paymentView.paymentStatus(), paymentView.paymentValue(), paymentView.updateAt(), paymentView.userId());
    }

}
