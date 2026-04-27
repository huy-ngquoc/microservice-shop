package vn.uit.edu.msshop.order.application.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.in.web.request.OrderDetailRequest;
import vn.uit.edu.msshop.order.adapter.in.web.response.InventoryResponse;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedSuccessDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.inventory.OrderCreatedInventoryDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.persistence.OrderOutbox;
import vn.uit.edu.msshop.order.adapter.out.persistence.OrderOutboxRepository;
import vn.uit.edu.msshop.order.adapter.remote.InventoryChecker;
import vn.uit.edu.msshop.order.application.dto.command.CreateOrderCommand;
import vn.uit.edu.msshop.order.application.port.in.CreateOrderUseCase;
import vn.uit.edu.msshop.order.application.port.out.CheckUserPort;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderDetailPort;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.application.port.out.SaveRedisStreamPort;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.CreateAt;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderDetail;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.OriginPrice;
import vn.uit.edu.msshop.order.domain.model.valueobject.PaymentStatus;
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
    private final OrderCreatedSuccessDocumentRepository orderCreatedSuccessDocumentRepo;
    private final OrderCreatedInventoryDocumentRepository orderCreatedInventoryDocumentRepository;
    private final SaveRedisStreamPort saveRedisStreamPort;
    private final OrderOutboxRepository orderOutboxRepo;
    
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
    //@Observed(name = "mongodb.save.order")
    public UUID create(CreateOrderCommand command) {
        this.checkUserPort.isUserAvailable(command.userId().value());
        List<OrderDetailRequest> requests= command.details().stream().map(item->new OrderDetailRequest(item.variantId(), item.quantity())).toList();
        List<OrderDetail> listDetails = loadOrderDetailPort.loadByListDetail(requests);
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
        .currency(command.currency())
        .paymentMethod(command.paymentMethod())
        .paymentStatus(new PaymentStatus("PENDING"))
        .build();
        final var saved = Order.create(draft);
        
    
   
        

        //final var saved = savePort.save(order);
        try {
            if(command.shippingInfo().email().equals("email@gmail.com")) {
        throw new RuntimeException("Simulate error");
    }
        manualCreate(saved);
        }
        catch(Exception e) {
            
                inventoryChecker.rollback(requests, "CREATE_ORDER_FAILED");
                throw new RuntimeException(e);
            
        }
        return saved.getId().value();
    }
   
    private void processOrder(List<OrderDetail> orderDetails) {
        List<OrderDetailRequest> requests = orderDetails.stream().map(item->new OrderDetailRequest(item.variantId(), item.amount())).toList();
        inventoryChecker.processOrder(requests);
    }
    private InventoryResponse findInListByVariantId(List<InventoryResponse> responses, UUID variantId) {
        for(InventoryResponse response:responses) {
            if(response.getVariantId().equals(variantId)) {
                return response;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public Order manualCreate(Order order) {
        if(order.getShippingInfo().email().equals("email@gmail.com")) {
        throw new RuntimeException("Simulate error");
    }
        /*OrderCreatedDocument outboxEventOrderCreated = OrderCreatedDocument.builder().currency(order.getCurrency().value())
        .orderId(order.getId().value())
        .paymentMethod(order.getPaymentMethod().value())
        .paymentValue(order.getTotalPrice().value())
        .userEmail(order.getShippingInfo().email())
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null).eventId(UUID.randomUUID())
        .lastError(null).userId(order.getUserId().value()).build();
        final var savedEvent=orderCreatedDocumentRepo.save(outboxEventOrderCreated);
         OrderCreatedSuccessDocument outboxOrderCreatedSuccess = OrderCreatedSuccessDocument.builder().eventId(UUID.randomUUID())
        .userId(order.getUserId().value())
        .variantIds(order.getDetails().stream().map(item->item.variantId()).toList())
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null).build();
        final var savedOutboxOrderCreatedSuccess = orderCreatedSuccessDocumentRepo.save(outboxOrderCreatedSuccess);
              final var result =savePort.save(order);
              TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                //publishPort.publishOrderCreated_InventoryEvent(savedOutboxEventOrderCreatedInventory);
                publishPort.publishOrderCreatedEvent(savedEvent);
                publishPort.publishClearCartEvent(savedOutboxOrderCreatedSuccess);
                
            }
        });*/
        final var result = savePort.save(order);
        final var details = result.getDetails().stream().map(item-> new OrderDetailRequest(item.variantId(), item.amount())).toList();
        final var orderOutbox = new OrderOutbox(UUID.randomUUID(), result.getId().value(), "PROCESS_ORDER", details, "PENDING","PENDING", Instant.now());
        orderOutboxRepo.save(orderOutbox);
        return  result;
    }


}
