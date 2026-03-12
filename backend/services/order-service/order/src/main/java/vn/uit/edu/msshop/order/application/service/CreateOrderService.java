package vn.uit.edu.msshop.order.application.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.application.dto.command.CreateOrderCommand;
import vn.uit.edu.msshop.order.application.port.in.CreateOrderUseCase;
import vn.uit.edu.msshop.order.application.port.out.CheckUserPort;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderDetailPort;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.CreateAt;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderDetail;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.OriginPrice;
import vn.uit.edu.msshop.order.domain.model.valueobject.TotalPrice;
import vn.uit.edu.msshop.order.domain.model.valueobject.UpdateAt;
/*@NonNull
    OrderId id,

    @NonNull
    ShippingInfo shippingInfo,

    @NonNull
    List<OrderDetail> details,

    @NonNull
    OrderStatus status,

    @NonNull
    UserId userId,

    @NonNull
    OriginPrice originPrice,

    @NonNull
    ShippingFee shippingFee,

    @NonNull
    Discount discount,

    @NonNull
    TotalPrice totalPrice,

    @NonNull
    CreateAt createAt,

    @NonNull
    UpdateAt updateAt */
@Service
@RequiredArgsConstructor
public class CreateOrderService implements CreateOrderUseCase {
    private final SaveOrderPort savePort;
    private final LoadOrderDetailPort loadOrderDetailPort;
    private final CheckUserPort checkUserPort;
    private final PublishOrderEventPort publishPort;

    @Override
    public void create(CreateOrderCommand command) {
        this.checkUserPort.isUserAvailable(command.userId().value());
        List<OrderDetail> listDetails = command.details().stream().map(item->{
            return loadOrderDetailPort.loadOrderDetail(item.variantId(), item.quantity());
        }).toList();
        long originPrice =0;
        for(OrderDetail d : listDetails) {
            originPrice+=d.amount()*d.unitPrice();
        }
        final var draft = Order.Draft.builder().id(command.id()).shippingInfo(command.shippingInfo())
        .details(listDetails)
        .status(new OrderStatus("PENDING"))
        .userId(command.userId())
        .originPrice(new OriginPrice(originPrice))
        .shippingFee(command.shippingFee())
        .discount(command.discount())
        .totalPrice(new TotalPrice(originPrice+command.shippingFee().value()-command.discount().value()))
        .createAt(new CreateAt(Instant.now()))
        .updateAt(new UpdateAt(null))
        .build();
        final var order = Order.create(draft);
        final var saved = savePort.save(order);
        
        
    }

}
