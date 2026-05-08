package vn.uit.edu.payment.application.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.payment.application.dto.query.PaymentView;
import vn.uit.edu.payment.domain.model.Payment;

@Component
public class PaymentViewMapper {
    public PaymentView toView(final Payment payment) {
        return new PaymentView(payment.getPaymentId().value().toString(), payment.getCreateAt().value(), payment.getCurrency().value(),
         payment.getOrderId().value().toString(), payment.getPaymentMethod().value(), payment.getPaymentStatus().value(), payment.getPaymentValue().value(),
          payment.getUpdateAt().value(), payment.getUserId().value().toString());
    }
}
