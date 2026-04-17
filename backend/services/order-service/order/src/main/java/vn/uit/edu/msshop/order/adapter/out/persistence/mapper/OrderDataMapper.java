package vn.uit.edu.msshop.order.adapter.out.persistence.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.order.adapter.out.persistence.OrderDetailDocument;
import vn.uit.edu.msshop.order.adapter.out.persistence.OrderDocument;
import vn.uit.edu.msshop.order.adapter.out.persistence.ShippingInfoDocument;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.CreateAt;
import vn.uit.edu.msshop.order.domain.model.valueobject.Currency;
import vn.uit.edu.msshop.order.domain.model.valueobject.Discount;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderDetail;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.OriginPrice;
import vn.uit.edu.msshop.order.domain.model.valueobject.PaymentMethod;
import vn.uit.edu.msshop.order.domain.model.valueobject.PaymentStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.ShippingFee;
import vn.uit.edu.msshop.order.domain.model.valueobject.ShippingInfo;
import vn.uit.edu.msshop.order.domain.model.valueobject.TotalPrice;
import vn.uit.edu.msshop.order.domain.model.valueobject.UpdateAt;
import vn.uit.edu.msshop.order.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.order.domain.model.valueobject.Version;

@Component
public class OrderDataMapper {
    public Order toDomain(OrderDocument document) {
        ShippingInfo shippingInfo = this.toShippingInfoDomain(document.getShippingInfo());
        List<OrderDetail> details = document.getDetails().stream().map(this::toOrderDetailDomain).toList();
        
        final var snapShot = Order.SnapShot.builder().id(new OrderId(document.getId()))
        .shippingInfo(shippingInfo)
        .details(details)
        .status(new OrderStatus(document.getStatus()))
        .userId(new UserId(document.getUserId()))
        .originPrice(new OriginPrice(document.getOriginPrice()))
        .shippingFee(new ShippingFee(document.getShippingFee()))
        .discount(new Discount(document.getDiscount()))
        .totalPrice(new TotalPrice(document.getTotalPrice()))
        .createAt(new CreateAt(document.getCreateAt()))
        .updateAt(new UpdateAt(document.getUpdateAt()))
        .version(new Version(document.getVersion()==null?null:document.getVersion()))
        .currency(new Currency(document.getCurrency()))
        .paymentMethod(new PaymentMethod(document.getPaymentMethod()))
        .paymentStatus(new PaymentStatus(document.getPaymentStatus()))
        .build();
        return Order.reconstitue(snapShot);
    }
    public ShippingInfo toShippingInfoDomain(ShippingInfoDocument document) {
        return new ShippingInfo(document.getFullName(),document.getAddress(),document.getPhone(),document.getEmail());
    }
    public OrderDetail toOrderDetailDomain(OrderDetailDocument orderDetailDocument) {
        
        return new OrderDetail(orderDetailDocument.getVariantId(), orderDetailDocument.getProductId(),orderDetailDocument.getProductName(), orderDetailDocument.getImageKey(), orderDetailDocument.getAmount(), orderDetailDocument.getUnitPrice(), orderDetailDocument.getTraits());
    }

    public OrderDocument toDocument(Order order) {
        ShippingInfoDocument shippingInfoDocument = this.toShippingInfoDocument(order.getShippingInfo());
        List<OrderDetailDocument> details = order.getDetails().stream().map(this::toOrderDetailDocument).toList();
        return new OrderDocument(order.getId().value(), shippingInfoDocument, details, order.getStatus().value(), 
        order.getUserId().value(), order.getOriginPrice().value(), order.getShippingFee().value(),
        order.getDiscount().value(), order.getTotalPrice().value(), order.getCreateAt().value(), order.getUpdateAt().value(),order.getVersion()==null ? null:order.getVersion().value(),order.getCurrency().value(), order.getPaymentMethod().value(), order.getPaymentStatus().value());
    }
    public ShippingInfoDocument toShippingInfoDocument(ShippingInfo shippingInfo) {
        return new ShippingInfoDocument(shippingInfo.fullName(), shippingInfo.address(), shippingInfo.email(), shippingInfo.phone());
    }
    public OrderDetailDocument toOrderDetailDocument(OrderDetail orderDetail) {
        return new OrderDetailDocument(orderDetail.variantId(), orderDetail.productName(), orderDetail.traits(), orderDetail.imageKey(), orderDetail.productId(), orderDetail.amount(), orderDetail.unitPrice());
    }
}
