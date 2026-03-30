package vn.uit.edu.msshop.order.application.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.exception.VariantNotEnoughException;
import vn.uit.edu.msshop.order.adapter.exception.VariantNotFoundException;
import vn.uit.edu.msshop.order.adapter.in.web.response.InventoryResponse;
import vn.uit.edu.msshop.order.adapter.out.event.OrderCreatedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.OrderCreatedDocumentRepository;
import vn.uit.edu.msshop.order.adapter.remote.InventoryChecker;
import vn.uit.edu.msshop.order.application.dto.command.CreateOrderCommand;
import vn.uit.edu.msshop.order.application.port.in.CreateOrderUseCase;
import vn.uit.edu.msshop.order.application.port.out.CheckUserPort;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderDetailPort;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderCreated;
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
    private final InventoryChecker inventoryChecker;
    private final OrderCreatedDocumentRepository orderCreatedDocumentRepo;
/*private String currency;
    private UUID orderId;
    private String paymentMethod;
    private long paymentValue;
    private String eventStatus;
    private Integer retryCount; 
    private Instant createdAt;
    private Instant updatedAt; 
    private String lastError; */
    @Override
    @Transactional
    public UUID create(CreateOrderCommand command) {
        this.checkUserPort.isUserAvailable(command.userId().value());
        List<OrderDetail> listDetails = command.details().stream().map(item->{
            return loadOrderDetailPort.loadOrderDetail(item.variantId(), item.quantity());
        }).toList();
        System.out.println(listDetails.get(0).variantId());
        canPlaceOrder(listDetails);
        long originPrice =0;
        for(OrderDetail d : listDetails) {
            originPrice+=d.amount()*d.unitPrice();
        }
        List<vn.uit.edu.msshop.order.domain.event.inventory.OrderDetail> event_details = listDetails.stream().map(item->new vn.uit.edu.msshop.order.domain.event.inventory.OrderDetail(
            item.variantId(), item.amount()
        )).toList();
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
        publishPort.publishOrderCreated_InventoryEvent(new OrderCreated(saved.getId().value(),event_details));
        OrderCreatedDocument outboxEventOrderCreated = OrderCreatedDocument.builder().currency(command.currency().value())
        .orderId(saved.getId().value())
        .paymentMethod(command.paymentMethod().value())
        .paymentValue(saved.getTotalPrice().value())
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null).build();
        final var savedEvent=orderCreatedDocumentRepo.save(outboxEventOrderCreated);
        System.out.println(event_details.size());
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishPort.publishOrderCreatedEvent(savedEvent);
            }
        });
        return saved.getId().value();
    }
    private void canPlaceOrder(List<OrderDetail> details) {
        List<UUID> variantIds = details.stream().map(item->item.variantId()).toList();
        List<InventoryResponse> responses = inventoryChecker.getInventoryBatch(variantIds);
        
        for(OrderDetail orderDetail: details) {
            InventoryResponse inventoryResponse = findInListByVariantId(responses, orderDetail.variantId());
            if(inventoryResponse==null) {
                throw new VariantNotFoundException(orderDetail.variantId());
            }
            if(inventoryResponse.getQuantity()<orderDetail.amount()) {
                throw new VariantNotEnoughException(orderDetail.variantId());
            }
        }
    }
    private InventoryResponse findInListByVariantId(List<InventoryResponse> responses, UUID variantId) {
        for(InventoryResponse response:responses) {
            if(response.getVariantId().equals(variantId)) {
                return response;
            }
        }
        return null;
    }

}
