package vn.uit.edu.payment.application.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.application.dto.command.CreatePaymentCommand;
import vn.uit.edu.payment.application.dto.query.PaymentView;
import vn.uit.edu.payment.application.mapper.PaymentViewMapper;
import vn.uit.edu.payment.application.port.in.CreateOnlinePaymentInfoUseCase;
import vn.uit.edu.payment.application.port.in.CreatePaymentUseCase;
import vn.uit.edu.payment.application.port.out.CheckOrderPort;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.application.port.out.PublishPaymentEventPort;
import vn.uit.edu.payment.application.port.out.SavePaymentPort;
import vn.uit.edu.payment.domain.event.PaymentCreated;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.CreateAt;
import vn.uit.edu.payment.domain.model.valueobject.UpdateAt;

@Service
@RequiredArgsConstructor
public class CreatePaymentService implements CreatePaymentUseCase {
    //private final CheckOrderPort checkOrderPort;
    private final SavePaymentPort savePort;
    private final PublishPaymentEventPort eventPort;
    private final PaymentViewMapper mapper;
    private final LoadPaymentPort loadPort;
    private final CreateOnlinePaymentInfoUseCase createOnlineUseCase;
    @Override
    @Transactional
    public PaymentView create(CreatePaymentCommand command) {
        //checkOrderPort.checkOrder(command.paymentId(), command.paymentValue(), command.orderId());
        Payment p = loadPort.loadPaymentByOrderId(command.orderId());
        if(p!=null) throw new RuntimeException("Payment for this order already exist");
        final var draft = Payment.Draft.builder().paymentId(command.paymentId()).createAt(new CreateAt(Instant.now())).currency(command.currency()).orderId(command.orderId()).paymentMethod(command.paymentMethod())
        .paymentStatus(command.paymentStatus()).paymentValue(command.paymentValue()).updateAt(new UpdateAt(null)).userId(command.userId()).userEmail(command.userEmail()).build();
        final var payment = Payment.create(draft);

        final var saved=savePort.save(payment);
        if(command.paymentMethod().value().equals("ONLINE")) {
            createOnlineUseCase.createPaymentLink(saved);
        }
        eventPort.publish(new PaymentCreated(saved.getPaymentId()));
        return mapper.toView(saved);
    }

}
