package vn.uit.edu.payment.adapter.out.remote;

import org.springframework.stereotype.Component;

import vn.uit.edu.payment.application.port.out.CheckOrderPort;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentValue;
@Component
public class OrderChecker implements CheckOrderPort{

    @Override
    public void checkOrder(PaymentId paymentId, PaymentValue paymentValue, OrderId orderId) {
        
    }

}
