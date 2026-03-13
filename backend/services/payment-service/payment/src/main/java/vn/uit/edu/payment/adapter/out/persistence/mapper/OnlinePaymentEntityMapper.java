package vn.uit.edu.payment.adapter.out.persistence.mapper;

import java.time.Instant;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.adapter.out.persistence.OnlinePaymentInfoJpaEntity;
import vn.uit.edu.payment.application.exception.PaymentNotFoundException;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.domain.model.OnlinePaymentInfo;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.CreateAt;
import vn.uit.edu.payment.domain.model.valueobject.OnlinePaymentNumber;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentLink;
import vn.uit.edu.payment.domain.model.valueobject.TransactionId;

@Component
@RequiredArgsConstructor
public class OnlinePaymentEntityMapper {
    private final LoadPaymentPort loadPaymentPort;
    private final PaymentEntityMapper mapper;
    public OnlinePaymentInfo toDomain(OnlinePaymentInfoJpaEntity entity) {
        return new OnlinePaymentInfo(new PaymentId(entity.getId()), new PaymentLink(entity.getPaymentLink()), new OnlinePaymentNumber(entity.getPaymentCode()), new TransactionId(entity.getTransactionId()), new CreateAt(Instant.now()));
    }
    public OnlinePaymentInfoJpaEntity toEntity(OnlinePaymentInfo domain) {
        Payment p = loadPaymentPort.loadPaymentById(domain.getPaymentId()).orElseThrow(()-> new PaymentNotFoundException(domain.getPaymentId()));
        return new OnlinePaymentInfoJpaEntity(domain.getPaymentId().value(), mapper.toEntity(p), domain.getPaymentNumber().value(), domain.getLink().value(), domain.getTransactionId().value(), domain.getCreateAt().value());
    }
}
