package vn.uit.edu.payment.application.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.uit.edu.payment.application.port.in.CreateOnlinePaymentInfoUseCase;
import vn.uit.edu.payment.application.port.out.SaveOnlinePaymentInfoPort;
import vn.uit.edu.payment.domain.model.OnlinePaymentInfo;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.CreateAt;
import vn.uit.edu.payment.domain.model.valueobject.OnlinePaymentNumber;
import vn.uit.edu.payment.domain.model.valueobject.PaymentLink;
import vn.uit.edu.payment.domain.model.valueobject.TransactionId;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateOnlinePaymentInfoService implements CreateOnlinePaymentInfoUseCase  {
    private final SaveOnlinePaymentInfoPort savePort;
    private final PayOS payOS;
    private static final String RETURN_URL="";
    private static final String CANCEL_URL="";
    private static final long PAYMENT_LINK_LIFETIME = 15*60;

    @Override
    @Transactional
    public String createPaymentLink(Payment payment) {
        String description = "Payment with id "+payment.getPaymentId().value().toString();

        ItemData item;
        item = ItemData.builder()
                .name("description")
                .quantity(1)
                .price((int)payment.getPaymentValue().value())
                .build();
        long orderCode=  System.currentTimeMillis();
        PaymentData paymentData = PaymentData.builder() 
                .orderCode(orderCode)
                .amount(1)
                .description(description)
                .items(List.of(item))
                .returnUrl(RETURN_URL)
                .cancelUrl(CANCEL_URL)
                .expiredAt((int)((System.currentTimeMillis() / 1000) + PAYMENT_LINK_LIFETIME))
                .build();
        try {
        CheckoutResponseData response = payOS.createPaymentLink(paymentData);
        OnlinePaymentInfo info = new OnlinePaymentInfo(payment.getPaymentId(), new PaymentLink(response.getCheckoutUrl()), new OnlinePaymentNumber(orderCode),new TransactionId(null), new CreateAt(Instant.now()));
        this.savePort.save(info);

        return response.getCheckoutUrl(); 
    } catch (Exception e) {
        throw new RuntimeException("Lỗi tạo link thanh toán");
    }

    }
}
