package vn.uit.edu.msshop.order.adapter.in.web;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.in.web.mapper.OrderWebMapper;
import vn.uit.edu.msshop.order.adapter.in.web.request.CreateOrderRequest;
import vn.uit.edu.msshop.order.adapter.in.web.request.UpdateOrderRequest;
import vn.uit.edu.msshop.order.adapter.in.web.response.OrderResponse;
import vn.uit.edu.msshop.order.application.port.in.CreateOrderUseCase;
import vn.uit.edu.msshop.order.application.port.in.FindOrderUseCase;
import vn.uit.edu.msshop.order.application.port.in.UpdateOrderUseCase;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.domain.event.OrderCreated;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.UserId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final CreateOrderUseCase createService;
    private final FindOrderUseCase findService;
    private final UpdateOrderUseCase updateService;
    private final OrderWebMapper mapper;
    private final PublishOrderEventPort eventPublisher;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable UUID id) {
        final var view = findService.findOrderById(new OrderId(id));
        return ResponseEntity.ok(this.mapper.toResponse(view));
    }

    @GetMapping()
    public ResponseEntity<Page<OrderResponse>> filter(@RequestParam Optional<UUID> variantId,@RequestParam Optional<OrderStatus> status,@RequestParam Optional<Integer> minPrice,
        @RequestParam Optional<Integer> maxPrice,@RequestParam Optional<UserId> userId,@RequestParam Optional<Instant> createFrom,
        @RequestParam Optional<Instant> createTo,@RequestParam int pageNumber,@RequestParam int pageSize) {
        final var view = findService.filterOrder(variantId, status, minPrice, maxPrice, userId, createFrom, createTo, pageNumber, pageSize);
        return ResponseEntity.ok(view.map(this.mapper::toResponse));
    }

    @PostMapping("/create") 
    public ResponseEntity<UUID> createOrder(@RequestBody CreateOrderRequest request) {
        final var command = this.mapper.toCommand(request);
        
        final var result= this.createService.create(command);
        eventPublisher.publishOrderCreatedEvent(new OrderCreated(request.currency(),result,request.paymentMethod(),request.totalPrice()));
        eventPublisher.publishClearCartEvent(mapper.toEvent(request));
        return ResponseEntity.ok(result);
    } 

    @PutMapping("/update")
    public ResponseEntity<Void> updateOrder(@RequestBody UpdateOrderRequest request) {
        final var command = this.mapper.toCommand(request);
        this.updateService.update(command);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/cod_success") 
    public ResponseEntity<Void> codOrderSuccess(@RequestParam UUID orderId) {
        this.updateService.codOrderSuccess(new OrderId(orderId));
        return ResponseEntity.noContent().build();

    }


}
