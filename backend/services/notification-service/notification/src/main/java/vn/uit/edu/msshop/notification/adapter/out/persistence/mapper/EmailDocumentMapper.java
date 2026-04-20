package vn.uit.edu.msshop.notification.adapter.out.persistence.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.notification.adapter.out.persistence.EmailDocument;
import vn.uit.edu.msshop.notification.adapter.out.persistence.OrderDetailDocument;
import vn.uit.edu.msshop.notification.adapter.out.persistence.OrderDocument;
import vn.uit.edu.msshop.notification.domain.model.Email;
import vn.uit.edu.msshop.notification.domain.model.OrderInfo;
import vn.uit.edu.msshop.notification.domain.model.valueobject.CreationTime;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailContent;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailId;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailStatus;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailTitle;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailType;
import vn.uit.edu.msshop.notification.domain.model.valueobject.OrderCurrency;
import vn.uit.edu.msshop.notification.domain.model.valueobject.OrderDetail;
import vn.uit.edu.msshop.notification.domain.model.valueobject.OrderDiscount;
import vn.uit.edu.msshop.notification.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.notification.domain.model.valueobject.OrderTotalPrice;
import vn.uit.edu.msshop.notification.domain.model.valueobject.OriginOrderValue;
import vn.uit.edu.msshop.notification.domain.model.valueobject.SendTime;
import vn.uit.edu.msshop.notification.domain.model.valueobject.UpdateTime;
import vn.uit.edu.msshop.notification.domain.model.valueobject.UserEmail;

@Component
public class EmailDocumentMapper {
    public OrderDetailDocument toDocument(OrderDetail orderDetail) {
        return new OrderDetailDocument(orderDetail.variantId(), orderDetail.productName(), orderDetail.unitPrice(), orderDetail.amount());
    }
    public OrderDocument toDocument(OrderInfo orderInfo) {
        List<OrderDetailDocument> orderDetailDocuments = orderInfo.getOrderDetails().stream().map(this::toDocument).toList();
        return new OrderDocument(orderInfo.getOrderId().value(), orderInfo.getOrderCurrency().value(), orderDetailDocuments, orderInfo.getOrderDiscount().value(), orderInfo.getOrderTotalPrice().value(), orderInfo.getOriginOrderValue().value());
    }
    public EmailDocument toDocument(Email email) {
        OrderDocument orderDocument =null;
        
        return new EmailDocument(email.getEmailId().value(), email.getEmailContent().value(), email.getEmailStatus().value(), email.getEmailContent().value(), email.getEmailType().value(), email.getOrderId().value(),email.getUserEmail().value(),email.getCreationTime().value(), email.getSendTime().value(), email.getUpdateTime().value());
    }
    public OrderDetail toDomain(OrderDetailDocument document) {
        return new OrderDetail(document.getVariantId(), document.getProductName(), document.getUnitPrice(), document.getAmount());
    }
    public OrderInfo toDomain(OrderDocument document) {
        List<OrderDetail> orderDetails = document.getOrderDetailDocuments().stream().map(this::toDomain).toList();
        return new OrderInfo(new OrderId(document.getOrderId()),new OrderCurrency(document.getOrderCurrency()), orderDetails, new OrderDiscount(document.getOrderDiscount()), new OrderTotalPrice(document.getOrderTotalPrice()), new OriginOrderValue(document.getOrderOriginValue()));
    }
    public Email toDomain(EmailDocument document) {
        
        return new Email(new EmailId(document.getEmailId()), new EmailContent(document.getEmailContent()), new EmailStatus(document.getEmailStatus()), new EmailTitle(document.getEmailTitle()), new EmailType(document.getEmailType()), new SendTime(document.getSendTime()), new UpdateTime(document.getUpdateTime()), new OrderId(document.getOrderId()),new UserEmail(document.getUserEmail()), new CreationTime(document.getCreationTime()));
    }
}
