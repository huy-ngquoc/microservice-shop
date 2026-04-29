package vn.uit.edu.payment.adapter.out.payos;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.payos.PayOS;
import vn.uit.edu.payment.application.port.out.CancellPaymentLinkPort;
import vn.uit.edu.payment.application.port.out.LoadOnlinePaymentInfoPort;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;

@Component
@RequiredArgsConstructor
public class CancelPaymentLinkAdapter implements CancellPaymentLinkPort {
    private final LoadOnlinePaymentInfoPort loadOnlinePaymentInfoPort;
    private final LoadPaymentPort loadPaymentPort;
    private final PayOS payOs;
    private static boolean isMockMode = true;

    @Override
    public void cancelPaymentLink(OrderId orderId) {
        if(isMockMode) return;
        final var payment = loadPaymentPort.loadPaymentByOrderId(orderId);
        if(payment==null) return;
        final var onlinePaymentInfo = loadOnlinePaymentInfoPort.loadById(payment.getPaymentId());
        if(onlinePaymentInfo==null) return;
        try {
    
    payOs.paymentRequests().cancel(onlinePaymentInfo.getPaymentNumber().value());
    
    
} catch (Exception e) {
    
    e.printStackTrace();
}

    }

}
