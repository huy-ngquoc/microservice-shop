package vn.uit.edu.payment.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.payment.application.port.in.LoadOnlinePaymentUseCase;
import vn.uit.edu.payment.application.port.out.LoadOnlinePaymentInfoPort;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoadOnlinePaymentInfoService implements LoadOnlinePaymentUseCase {

    private final LoadOnlinePaymentInfoPort loadPort;

    @Override
    public String getPaymentLink(OrderId orderId) {
        return loadPort.getPaymentLinkByOrderId(orderId);
    }

}
