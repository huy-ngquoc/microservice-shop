package vn.uit.edu.msshop.order.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.application.dto.command.UpdateOrderCommand;
import vn.uit.edu.msshop.order.application.exception.OrderNotFoundException;
import vn.uit.edu.msshop.order.application.port.in.UpdateOrderUseCase;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderPort;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.domain.event.CodPaymentCancelled;
import vn.uit.edu.msshop.order.domain.event.CodPaymentReceived;
import vn.uit.edu.msshop.order.domain.event.OrderUpdated;
import vn.uit.edu.msshop.order.domain.model.Order;
/*@NonNull
        OrderId  id,
        ShippingInfo shippingInfo,
        OrderStatus orderStatus */
@Service
@RequiredArgsConstructor
public class UpdateOrderService implements UpdateOrderUseCase {
    private final LoadOrderPort loadOrderPort;
    private final SaveOrderPort saveOrderPort;
    private final PublishOrderEventPort publishEventPort;

    @Override
    public void update(UpdateOrderCommand command) {
        Order order = loadOrderPort.loadById(command.id()).orElseThrow(()->new OrderNotFoundException(command.id()));
        final var updateInfo = Order.UpdateInfo.builder().id(command.id()).shippingInfo(command.shippingInfo().apply(order.getShippingInfo())).orderStatus(command.status().apply(order.getStatus())).build();
        final var next = order.applyUpdateInfo(updateInfo);
        final var saved = saveOrderPort.save(next);
        
        if(saved.getStatus().value().equals("CANCELLED")) {
            publishEventPort.publishCodPaymentCancelled(new CodPaymentCancelled(saved.getId().value()));
        }
        if(saved.getStatus().value().equals("RECEIVED")) {
            publishEventPort.publishCodPaymentReceived(new CodPaymentReceived(saved.getId().value()));
        }
        publishEventPort.publish(new OrderUpdated(saved.getId()));
    }

}
