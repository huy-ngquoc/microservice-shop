package vn.uit.edu.payment.application.service;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;
import vn.payos.PayOS;
import vn.payos.model.webhooks.Webhook;
import vn.payos.model.webhooks.WebhookData;
import vn.uit.edu.payment.adapter.in.web.response.OrderResponse;
import vn.uit.edu.payment.adapter.out.event.documents.OnlinePaymentSuccessDocument;
import vn.uit.edu.payment.adapter.out.event.documents.PaymentSuccessDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.OnlinePaymentSuccessRepository;
import vn.uit.edu.payment.adapter.out.event.repositories.PaymentSuccessDocumentRepository;
import vn.uit.edu.payment.adapter.out.persistence.PaybackPaymentRepository;
import vn.uit.edu.payment.adapter.out.persistence.PaybackPayments;
import vn.uit.edu.payment.adapter.out.remote.OrderChecker;
import vn.uit.edu.payment.application.exception.PaymentNotFoundException;
import vn.uit.edu.payment.application.port.out.LoadOnlinePaymentInfoPort;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.application.port.out.PayOsWebHookPort;
import vn.uit.edu.payment.application.port.out.PublishPaymentEventPort;
import vn.uit.edu.payment.application.port.out.SaveOnlinePaymentInfoPort;
import vn.uit.edu.payment.application.port.out.SavePaymentPort;
import vn.uit.edu.payment.domain.model.OnlinePaymentInfo;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.OnlinePaymentNumber;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentStatus;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayOsWebHookService implements PayOsWebHookPort {
    private final PayOS payOS;
    private final SaveOnlinePaymentInfoPort saveOnlinePaymentInfoPort;
    private final LoadOnlinePaymentInfoPort loadOnlinePaymentInfoPort;
    private final SavePaymentPort savePaymentPort;
    private final LoadPaymentPort loadPaymentPort;
    private final PublishPaymentEventPort eventPort;
    private final PaymentSuccessDocumentRepository paymentSuccessRepo;
    private final PublishPaymentEventPort publishEventPort;
    private final OnlinePaymentSuccessRepository onlinePaymentSuccessRepo;
    private final PaybackPaymentRepository paybackPaymentRepo;
    private final OrderChecker orderChecker;
    private final ObjectMapper objectMapper;

    // private static boolean isMockMode=true;
    @Override

    public void handlePayOSWebHook(
            Webhook body) {
        WebhookData webhookData;
        try {

            webhookData = payOS.webhooks().verify(body);
        } catch (Exception ex) {
            log.error("Xác thực Webhook thất bại! Có thể là request giả mạo.");
            return;
        }

        if ("success".equalsIgnoreCase(webhookData.getDesc()) || "00".equals(webhookData.getCode())) {
            try {
                this.processPaymentUpdate(webhookData);
            } catch (Exception ex) {
                log.error("Lỗi xử lý cập nhật: ", ex);
                throw ex;
            }
        } else {
            log.info("Nhận Webhook nhưng không phải trạng thái thành công (Desc: {}), bỏ qua.", webhookData.getDesc());
            // Có thể xử lý thêm logic HỦY đơn ở đây nếu desc báo khách bấm Hủy
        }
    }

    private WebhookData mapBodyToWebhookData(
            Webhook body) {
        try {
            // body.getData() trong SDK của PayOS thường trả về một Object/LinkedHashMap
            // Chúng ta dùng ObjectMapper để convert nó sang class WebhookData
            return objectMapper.convertValue(body.getData(), WebhookData.class);
        } catch (Exception e) {
            log.error("Không thể map dữ liệu Webhook: ", e);
            throw new RuntimeException("Lỗi chuyển đổi dữ liệu Mock Webhook");
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public void processPaymentUpdate(
            WebhookData webhookData) {
        long orderCode = webhookData.getOrderCode();
        log.info("Bắt đầu xử lý Webhook cho OrderCode: {}", orderCode);

        // 1. Kiểm tra mã test
        if (orderCode == 123)
            return;

        // 2. Tìm kiếm thông tin Payment
        OnlinePaymentInfo onlinePaymentInfo = loadOnlinePaymentInfoPort
                .loadByOrderCode(new OnlinePaymentNumber(orderCode));
        Payment payment = loadPaymentPort.loadPaymentById(onlinePaymentInfo.getPaymentId())
                .orElseThrow(() -> new PaymentNotFoundException(onlinePaymentInfo.getPaymentId()));

        // 3. CHIẾN THUẬT IDEMPOTENCY: Nếu payment đã xử lý xong thì thoát ngay

        try {
            // 4. Kiểm tra trạng thái Đơn hàng bên Order Service
            boolean shouldPayback = false;
            try {
                ResponseEntity<OrderResponse> response = orderChecker.getById(payment.getOrderId().value());
                if (response.getBody() == null || (!"WAITING_PAYMENT".equals(response.getBody().status())
                        && !"CONFIRMED".equals(response.getBody().status()))) {
                    shouldPayback = true;
                }
            } catch (FeignException e) {
                if (e.status() == 404)
                    shouldPayback = true;
                else
                    throw e; // Lỗi mạng khác thì throw để Kafka/Webhook retry lại
            }

            // 5. Rẽ nhánh xử lý
            if (shouldPayback) {
                handlePaybackLogic(payment);
            } else {
                handlePaymentSuccessLogic(payment);
            }

        } catch (org.springframework.orm.ObjectOptimisticLockingFailureException e) {
            // 6. XỬ LÝ LỖI STALE OBJECT: Có thread khác đã nhanh tay update version trước
            log.warn("Xung đột Version (Optimistic Lock) cho đơn {}. Webhook trùng đã được xử lý an toàn.", orderCode);
            // Không ném lỗi ra ngoài để tránh Webhook tiếp tục retry vô hạn
        }
    }

    /**
     * Xử lý hoàn tiền khi đơn hàng không hợp lệ
     */
    private void handlePaybackLogic(
            Payment payment) {
        // Check trùng ID để tránh StaleObjectStateException khi insert cùng lúc
        if (!paybackPaymentRepo.existsById(payment.getOrderId().value())) {
            paybackPaymentRepo.save(new PaybackPayments(
                    payment.getOrderId().value(),
                    payment.getUserId().value(),
                    payment.getPaymentValue().value()));
            log.info("Đã ghi nhận yêu cầu hoàn tiền cho đơn: {}", payment.getOrderId().value());
        }
    }

    /**
     * Xử lý cập nhật thanh toán thành công và ghi Outbox Event
     */
    private void handlePaymentSuccessLogic(
            Payment payment) {
        // Cập nhật trạng thái Payment sang SUCCESS
        Payment.UpdateInfo updateInfo = Payment.UpdateInfo.builder()
                .paymentId(payment.getPaymentId())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(new PaymentStatus("SUCCESS"))
                .build();

        Payment saved = savePaymentPort.save(payment.applyUpdateInfo(updateInfo));

        // Tạo các tài liệu sự kiện (Outbox Pattern)
        PaymentSuccessDocument paymentEvent = createPaymentSuccessEvent(saved);
        OnlinePaymentSuccessDocument onlineEvent = createOnlinePaymentSuccessEvent(saved);

        final var savedPaymentSuccessEvent = paymentSuccessRepo.save(paymentEvent);
        final var savedOnlineEvent = onlinePaymentSuccessRepo.save(onlineEvent);

        // Đăng ký bắn tin nhắn sau khi DB commit thành công
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                eventPort.publishOnlinePaymentSuccess(savedOnlineEvent);
                eventPort.publishPaymentSuccess(savedPaymentSuccessEvent);
                log.info("Đã publish thành công các event thanh toán cho đơn: {}", saved.getOrderId().value());
            }
        });
    }

    // --- Helper methods để tạo Document ---
    private PaymentSuccessDocument createPaymentSuccessEvent(
            Payment p) {
        return PaymentSuccessDocument.builder()
                .eventId(UUIDs.newId())
                .orderId(p.getOrderId().value())
                .eventStatus("PENDING").retryCount(0)
                .createdAt(Instant.now()).build();
    }

    private OnlinePaymentSuccessDocument createOnlinePaymentSuccessEvent(
            Payment p) {
        return OnlinePaymentSuccessDocument.builder()
                .eventId(UUIDs.newId())
                .orderId(p.getOrderId().value())
                .userEmail(p.getUserEmail().value())
                .userId(p.getUserId().value())
                .eventStatus("PENDING").retryCount(0)
                .createdAt(Instant.now()).build();
    }

    @Override
    public void fakeWebHook(
            UUID orderId) {
        Payment payment = loadPaymentPort.loadPaymentByOrderId(new OrderId(orderId));
        if (payment == null)
            return;
        Random rand = new Random();
        int randNumber = rand.nextInt(10);
        if (randNumber <= 3)
            return;
        handlePaymentSuccessLogic(payment);
    }

}
