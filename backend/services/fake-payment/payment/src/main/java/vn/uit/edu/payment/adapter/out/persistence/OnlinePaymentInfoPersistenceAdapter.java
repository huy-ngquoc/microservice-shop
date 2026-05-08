package vn.uit.edu.payment.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.adapter.out.persistence.mapper.OnlinePaymentEntityMapper;
import vn.uit.edu.payment.application.exception.OnlinePaymentInfoNotFoundException;
import vn.uit.edu.payment.application.exception.PaymentNotFoundException;
import vn.uit.edu.payment.application.port.out.LoadOnlinePaymentInfoPort;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.application.port.out.SaveOnlinePaymentInfoPort;
import vn.uit.edu.payment.domain.model.OnlinePaymentInfo;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.OnlinePaymentNumber;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;

@Service
@RequiredArgsConstructor
public class OnlinePaymentInfoPersistenceAdapter implements LoadOnlinePaymentInfoPort, SaveOnlinePaymentInfoPort {
    private final SpringDataOnlinePaymentInfoJpaRepository repo;
    private final OnlinePaymentEntityMapper mapper;
    private final LoadPaymentPort loadPaymentPort;

    @Override
    public OnlinePaymentInfo loadById(
            PaymentId id) {
        final var result = repo.findById(id.value()).orElseThrow(() -> new OnlinePaymentInfoNotFoundException(id));
        return mapper.toDomain(result);
    }

    @Override
    public OnlinePaymentInfo save(
            OnlinePaymentInfo onlinePaymentInfo) {
        final var entity = mapper.toEntity(onlinePaymentInfo);
        final var result = repo.save(entity);
        return mapper.toDomain(result);
    }

    @Override
    public OnlinePaymentInfo loadByOrderCode(
            OnlinePaymentNumber paymentNumber) {
        final var result = repo.findByPaymentCode(paymentNumber.value())
                .orElseThrow(() -> new OnlinePaymentInfoNotFoundException(paymentNumber));
        return mapper.toDomain(result);
    }

    @Override
    public List<OnlinePaymentInfo> loadByPayments(
            List<Payment> payments) {
        final var result = repo.findByPayment_IdIn(payments.stream().map(item -> item.getPaymentId().value()).toList());
        return result.stream().map(item -> mapper.toDomain(item)).toList();
    }

    @Override
    public String getPaymentLinkByOrderId(
            OrderId orderId) {
        Payment p = loadPaymentPort.loadPaymentByOrderId(orderId);
        if (p == null)
            throw new PaymentNotFoundException(orderId);
        final var entity = repo.findById(p.getPaymentId().value())
                .orElseThrow(() -> new OnlinePaymentInfoNotFoundException(p.getPaymentId()));
        final var result = mapper.toDomain(entity);
        return result.getLink().value();

    }

}
