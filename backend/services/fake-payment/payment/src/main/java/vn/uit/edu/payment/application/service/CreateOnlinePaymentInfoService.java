package vn.uit.edu.payment.application.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.uit.edu.payment.adapter.out.event.documents.PaymentLinkCreatedDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.PaymentLinkCreatedRepository;
import vn.uit.edu.payment.application.port.in.CreateOnlinePaymentInfoUseCase;
import vn.uit.edu.payment.application.port.out.PublishPaymentEventPort;
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
public class CreateOnlinePaymentInfoService implements CreateOnlinePaymentInfoUseCase {
    private final SaveOnlinePaymentInfoPort savePort;
    private final PayOS payOS;
    private static final String RETURN_URL = "";
    private static final String CANCEL_URL = "";
    private static final long PAYMENT_LINK_LIFETIME = 15 * 60;
    private final PublishPaymentEventPort publishEventPort;
    private final PaymentLinkCreatedRepository paymentLinkCreatedRepo;

    @Override
    @Transactional
    public String createPaymentLink(
            Payment payment) {

        /*
         * long orderCode= System.currentTimeMillis();
         * CreatePaymentLinkRequest request = CreatePaymentLinkRequest.builder()
         * .orderCode(orderCode)
         * .amount(payment.getPaymentValue().value())
         * .description("Payment")
         * .returnUrl(RETURN_URL)
         * .cancelUrl(CANCEL_URL)
         * .expiredAt((System.currentTimeMillis() / 1000)+PAYMENT_LINK_LIFETIME)
         * .build();
         * var paymentLink = payOS.paymentRequests().create(request);
         *
         * OnlinePaymentInfo info = new OnlinePaymentInfo(payment.getPaymentId(), new
         * PaymentLink(paymentLink.getCheckoutUrl()), new
         * OnlinePaymentNumber(orderCode),new TransactionId(null), new
         * CreateAt(Instant.now()));
         * this.savePort.save(info);
         * String result = paymentLink.getCheckoutUrl();
         * var paymentLinkCreatedDocument = PaymentLinkCreatedDocument.builder()
         * .eventId(UUIDs.newId())
         * .paymentLink(result)
         * .orderId(payment.getOrderId().value())
         * .userEmail(payment.getUserEmail().value())
         * .userId(payment.getUserId().value())
         * .eventStatus("PENDING")
         * .retryCount(0)
         * .createdAt(Instant.now())
         * .updatedAt(null)
         * .lastError(null).build();
         * publishEventPort.publishPaymentLinkCreated(paymentLinkCreatedRepo.save(
         * paymentLinkCreatedDocument));
         *
         * return result;
         */
        String result = UUIDs.newId().toString();

        OnlinePaymentInfo info = new OnlinePaymentInfo(payment.getPaymentId(), new PaymentLink(result),
                new OnlinePaymentNumber(System.currentTimeMillis()), new TransactionId(null),
                new CreateAt(Instant.now()));
        this.savePort.save(info);
        var paymentLinkCreatedDocument = PaymentLinkCreatedDocument.builder()
                .eventId(UUIDs.newId())
                .paymentLink(result)
                .orderId(payment.getOrderId().value())
                .userEmail(payment.getUserEmail().value())
                .userId(payment.getUserId().value())
                .eventStatus("PENDING")
                .retryCount(0)
                .createdAt(Instant.now())
                .updatedAt(null)
                .lastError(null).build();
        publishEventPort.publishPaymentLinkCreated(paymentLinkCreatedRepo.save(paymentLinkCreatedDocument));

        return result;

    }

}
