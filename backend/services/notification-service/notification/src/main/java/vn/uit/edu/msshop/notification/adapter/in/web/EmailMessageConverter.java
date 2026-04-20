package vn.uit.edu.msshop.notification.adapter.in.web;

import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.notification.application.dto.command.CreateEmailCommand;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailContent;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailTitle;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailType;
import vn.uit.edu.msshop.notification.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.notification.domain.model.valueobject.UserEmail;

@Component
public class EmailMessageConverter {
    private static final String ORDER_PREFIX = "http://localhost:8000/order/";
    public CreateEmailCommand toCommand(String emailContent, String emailTitle, String emailType, UUID orderId, String userEmail) {
        return new CreateEmailCommand(new EmailContent(emailContent), new EmailTitle(emailTitle), new EmailType(emailType), new OrderId(orderId), new UserEmail(userEmail));
    }
    public String getCodOrderCreatedContent(UUID orderId) {
        StringBuilder content = new StringBuilder();
    
    content.append("Chào bạn,\n\n");
    content.append("MSShop đã tiếp nhận đơn hàng của bạn. Dưới đây là thông tin quan trọng:\n\n");
    
    
    content.append("1. Xem chi tiết đơn hàng tại đây:\n");
    content.append(ORDER_PREFIX).append(orderId.toString()).append("\n\n"); 
    
    
    content.append("2. Hình thức thanh toán là COD, vui lòng thanh toán khi nhận được hàng\n");
    
    
    content.append("Cảm ơn bạn đã ủng hộ MSShop!\n");
    content.append("Đội ngũ hỗ trợ khách hàng.");
    return content.toString();
    }
    public String getOrderCancelledContent(UUID orderId) {
        StringBuilder content = new StringBuilder();
        content.append("Chào bạn,\n\n");
    content.append("MSShop đã ghi nhận yêu cầu hủy đơn hàng của bạn. Dưới đây là thông tin quan trọng:\n\n");
    
    
    content.append("1. Xem chi tiết đơn hàng tại đây:\n");
    content.append(ORDER_PREFIX).append(orderId.toString()).append("\n\n"); 
    
    
    
    
    
    content.append("Cảm ơn bạn đã ủng hộ MSShop!\n");
    content.append("Đội ngũ hỗ trợ khách hàng.");
    return content.toString();
    }
    public String getOrderShippedContent(UUID orderId) {
        StringBuilder content = new StringBuilder();
        content.append("Chào bạn,\n\n");
    content.append("MSShop đã chuyển hàng cho bạn. Dưới đây là thông tin quan trọng:\n\n");
    
    
    content.append("1. Xem chi tiết đơn hàng tại đây:\n");
    content.append(ORDER_PREFIX).append(orderId.toString()).append("\n\n"); 
    
    content.append("2. Vui lòng xác nhận khi bạn đã nhận được hàng\n");
    
    
    
    content.append("Cảm ơn bạn đã ủng hộ MSShop!\n");
    content.append("Đội ngũ hỗ trợ khách hàng.");
    return content.toString();
    }

    public String getOrderReceivedContent(UUID orderId) {
        StringBuilder content = new StringBuilder();
        content.append("Chào bạn,\n\n");
    content.append("Cảm ơn bạn đã xác nhận đơn hàng. Dưới đây là thông tin quan trọng:\n\n");
    
    
    content.append("1. Xem chi tiết đơn hàng tại đây:\n");
    content.append(ORDER_PREFIX).append(orderId.toString()).append("\n\n"); 
    
    content.append("Cảm ơn bạn đã tin tưởng và sử dụng dịch vụ của chúng tối\n");
    
    
    
    content.append("Cảm ơn bạn đã ủng hộ MSShop!\n");
    content.append("Đội ngũ hỗ trợ khách hàng.");
    return content.toString();
    }
    public String getPaymentLinkCreatedContent(UUID orderId, String paymentLink) {
        StringBuilder content = new StringBuilder();
    
    content.append("Chào bạn,\n\n");
    content.append("MSShop đã tiếp nhận đơn hàng của bạn. Dưới đây là thông tin quan trọng:\n\n");
    
    // Khối 1: Link xem chi tiết đơn
    content.append("1. Xem chi tiết đơn hàng tại đây:\n");
    content.append(ORDER_PREFIX).append(orderId.toString()).append("\n\n"); 
    
    // Khối 2: Link thanh toán (Ví dụ PayOS)
    content.append("2. Để hoàn tất, vui lòng thanh toán qua liên kết này:\n");
    content.append(paymentLink).append("\n\n");
    
    content.append("Cảm ơn bạn đã ủng hộ MSShop!\n");
    content.append("Đội ngũ hỗ trợ khách hàng.");

    // Gán vào Value Object của bạn
    return content.toString();
    }
    public String getPaymentLinkExpiredContent(UUID orderId) {
        StringBuilder content = new StringBuilder();
    
    content.append("Chào bạn,\n\n");
    content.append("Đơn hàng của bạn đã bị hủy do hết hạn thanh toán. Dưới đây là thông tin quan trọng:\n\n");
    
    
    content.append("1. Xem chi tiết đơn hàng tại đây:\n");
    content.append(ORDER_PREFIX).append(orderId.toString()).append("\n\n"); 
    
    
    content.append("Chúng tôi xin lỗi vì sự bất tiện này\n");
    
    
    content.append("Cảm ơn bạn đã ủng hộ MSShop!\n");
    content.append("Đội ngũ hỗ trợ khách hàng.");
    return content.toString();
    }

    public String getPaymentOnlineSuccessExpiredContent(UUID orderId) {
        StringBuilder content = new StringBuilder();
    
    content.append("Chào bạn,\n\n");
    content.append("Cảm ơn bạn đã thanh toán, đơn hàng của bạn đang được xử lý. Dưới đây là thông tin quan trọng:\n\n");
    
    
    content.append("1. Xem chi tiết đơn hàng tại đây:\n");
    content.append(ORDER_PREFIX).append(orderId.toString()).append("\n\n"); 
    
    
    content.append("Chúng tôi sẽ thông báo khi đơn hàng được cập nhật\n");
    
    
    content.append("Cảm ơn bạn đã ủng hộ MSShop!\n");
    content.append("Đội ngũ hỗ trợ khách hàng.");
    return content.toString();
    }
}
