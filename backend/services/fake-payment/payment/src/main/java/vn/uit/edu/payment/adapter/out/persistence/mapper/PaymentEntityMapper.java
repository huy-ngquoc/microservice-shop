package vn.uit.edu.payment.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.payment.adapter.out.persistence.PaymentJpaEntity;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.CreateAt;
import vn.uit.edu.payment.domain.model.valueobject.Currency;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentMethod;
import vn.uit.edu.payment.domain.model.valueobject.PaymentStatus;
import vn.uit.edu.payment.domain.model.valueobject.PaymentValue;
import vn.uit.edu.payment.domain.model.valueobject.UpdateAt;
import vn.uit.edu.payment.domain.model.valueobject.UserEmail;
import vn.uit.edu.payment.domain.model.valueobject.UserId;
/*PaymentId paymentId,
        CreateAt createAt,
        Currency currency,
        OrderId orderId,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        PaymentValue paymentValue,
        UpdateAt updateAt */

@Component
public class PaymentEntityMapper {
    public Payment toDomain(
            PaymentJpaEntity e) {
        final var snapshot = Payment.Snapshot.builder().paymentId(new PaymentId(e.getId()))
                .createAt(new CreateAt(e.getCreateAt()))
                .currency(new Currency(e.getCurrency()))
                .orderId(new OrderId(e.getOrderId()))
                .paymentMethod(new PaymentMethod(e.getPaymentMethod()))
                .paymentValue(new PaymentValue(e.getPaymentValue()))
                .paymentStatus(new PaymentStatus(e.getPaymentStatus()))
                .updateAt(new UpdateAt(e.getUpdateAt()))
                .userId(new UserId(e.getUserId()))
                .userEmail(new UserEmail(e.getUserEmail()))
                .build();
        return Payment.reconstitue(snapshot);
    }

    public PaymentJpaEntity toEntity(
            Payment p) {
        final var snapshot = p.snapshot();
        return PaymentJpaEntity.of(snapshot.paymentId().value(), snapshot.createAt().value(),
                snapshot.updateAt().value(), snapshot.currency().value(), snapshot.orderId().value(),
                snapshot.paymentMethod().value(), snapshot.paymentStatus().value(), snapshot.paymentValue().value(),
                snapshot.userId().value(), snapshot.userEmail().value());
    }
}
