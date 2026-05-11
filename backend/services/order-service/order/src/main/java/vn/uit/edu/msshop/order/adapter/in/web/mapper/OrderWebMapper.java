package vn.uit.edu.msshop.order.adapter.in.web.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;
import vn.uit.edu.msshop.order.adapter.in.web.request.CreateOrderRequest;
import vn.uit.edu.msshop.order.adapter.in.web.request.OrderDetailRequest;
import vn.uit.edu.msshop.order.adapter.in.web.request.UpdateOrderRequest;
import vn.uit.edu.msshop.order.adapter.in.web.request.common.ChangeRequest;
import vn.uit.edu.msshop.order.adapter.in.web.response.OrderDetailResponse;
import vn.uit.edu.msshop.order.adapter.in.web.response.OrderResponse;
import vn.uit.edu.msshop.order.application.common.Change;
import vn.uit.edu.msshop.order.application.dto.command.CreateOrderCommand;
import vn.uit.edu.msshop.order.application.dto.command.OrderDetailCommand;
import vn.uit.edu.msshop.order.application.dto.command.UpdateOrderCommand;
import vn.uit.edu.msshop.order.application.dto.query.OrderView;
import vn.uit.edu.msshop.order.domain.model.valueobject.Currency;
import vn.uit.edu.msshop.order.domain.model.valueobject.Discount;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderDetail;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.PaymentMethod;
import vn.uit.edu.msshop.order.domain.model.valueobject.ShippingFee;
import vn.uit.edu.msshop.order.domain.model.valueobject.ShippingInfo;
import vn.uit.edu.msshop.order.domain.model.valueobject.UserId;

/*String orderId,
    String fullName,
    String address,
    String email,
    String phone,
    List<OrderDetailResponse> detailResponses,
    long shippingFee,
    long discount,
    String status,
    Instant createAt */
@Component
public class OrderWebMapper {

    public CreateOrderCommand toCommand(
            CreateOrderRequest request) {
        ShippingInfo shippingInfo = new ShippingInfo(request.fullName(), request.address(), request.phone(),
                request.email());
        List<OrderDetailCommand> details = request.detailRequests().stream().map(this::toOrderDetailCommand).toList();
        return new CreateOrderCommand(new OrderId(UUIDs.newId()), shippingInfo, details, new UserId(request.userId()),
                new ShippingFee(request.shippingFee()), new Discount(request.discount()),
                new Currency(request.currency()), new PaymentMethod(request.paymentMethod()));
    }

    public OrderDetailCommand toOrderDetailCommand(
            OrderDetailRequest detailRequest) {
        return new OrderDetailCommand(detailRequest.variantId(), detailRequest.quantity());
    }

    public UpdateOrderCommand toCommand(
            UpdateOrderRequest request) {
        String fullName = request.fullName().value();
        String address = request.address().value();
        String email = request.email().value();
        String phone = request.phone().value();
        ShippingInfo shippingInfo = new ShippingInfo(fullName, address, phone, email);
        Change<ShippingInfo> changeShippingInfo = Change.set(shippingInfo);
        Change<OrderStatus> changeOrderStatus = ChangeRequest.toChange(request.orderStatus(), OrderStatus::new);
        return new UpdateOrderCommand(new OrderId(request.id()), changeShippingInfo, changeOrderStatus);
    }

    public OrderDetailResponse toResponse(
            OrderDetail orderDetail) {
        return new OrderDetailResponse(orderDetail.variantId().toString(), orderDetail.productId().toString(),
                orderDetail.productName(), orderDetail.traits(), orderDetail.amount(), orderDetail.unitPrice());
    }

    public OrderResponse toResponse(
            OrderView orderView) {
        List<OrderDetailResponse> detailResponses = orderView.details().stream().map(item -> toResponse(item)).toList();
        return new OrderResponse(orderView.orderId().toString(), orderView.shippingInfo().fullName(),
                orderView.shippingInfo().address(), orderView.shippingInfo().email(), orderView.shippingInfo().phone(),
                detailResponses, orderView.shippingFee().value(), orderView.discount().value(),
                orderView.status().value(), orderView.createAt().value(), orderView.updateAt().value(),
                orderView.currency().value(), orderView.paymentMethod().value(), orderView.paymentStatus().value());
    }
    /*
     * public OrderCreatedSuccess toEvent(CreateOrderRequest request) {
     * return new
     * OrderCreatedSuccess(request.userId(),request.detailRequests().stream().map(
     * item->item.variantId()).toList());
     * }
     */

}
