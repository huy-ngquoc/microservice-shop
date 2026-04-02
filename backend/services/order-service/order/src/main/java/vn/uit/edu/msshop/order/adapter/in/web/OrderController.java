package vn.uit.edu.msshop.order.adapter.in.web;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.exception.VariantNotEnoughException;
import vn.uit.edu.msshop.order.adapter.exception.VariantNotFoundException;
import vn.uit.edu.msshop.order.adapter.in.web.mapper.OrderWebMapper;
import vn.uit.edu.msshop.order.adapter.in.web.request.CreateOrderRequest;
import vn.uit.edu.msshop.order.adapter.in.web.request.UpdateOrderRequest;
import vn.uit.edu.msshop.order.adapter.in.web.response.OrderResponse;
import vn.uit.edu.msshop.order.application.port.in.CheckPermissionUseCase;
import vn.uit.edu.msshop.order.application.port.in.CreateOrderUseCase;
import vn.uit.edu.msshop.order.application.port.in.FindOrderUseCase;
import vn.uit.edu.msshop.order.application.port.in.UpdateOrderUseCase;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
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
    private final CheckPermissionUseCase checkPermission;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable UUID id,@RequestHeader("X-User-Id") String userFromHeader, @RequestHeader("X-User-Roles") String role) {
        
        final var view = findService.findOrderById(new OrderId(id));
        if(!checkPermission.isAdmin(role)&&!checkPermission.isSameUser(userFromHeader,view.userId().value().toString() )) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(this.mapper.toResponse(view));
    }

    @GetMapping()
    public ResponseEntity<Page<OrderResponse>> filter(@RequestParam Optional<UUID> variantId,@RequestParam Optional<OrderStatus> status,@RequestParam Optional<Integer> minPrice,
        @RequestParam Optional<Integer> maxPrice,@RequestParam Optional<UserId> userId,@RequestParam Optional<Instant> createFrom,
        @RequestParam Optional<Instant> createTo,@RequestParam int pageNumber,@RequestParam int pageSize,@RequestHeader("X-User-Id") String userFromHeader, @RequestHeader("X-User-Roles") String role) {
        if(!checkPermission.isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        final var view = findService.filterOrder(variantId, status, minPrice, maxPrice, userId, createFrom, createTo, pageNumber, pageSize);

        return ResponseEntity.ok(view.map(this.mapper::toResponse));
    }

    @PostMapping("/create") 
    public ResponseEntity<UUID> createOrder(@RequestBody CreateOrderRequest request,@RequestHeader("X-User-Id") String userFromHeader, @RequestHeader("X-User-Roles") String role) {
        if(!checkPermission.isUser(role)&&!checkPermission.isSameUser(userFromHeader,request.userId().toString())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        final var command = this.mapper.toCommand(request);
        try {
        final var result= this.createService.create(command);
        //eventPublisher.publishOrderCreatedEvent(new OrderCreated(request.currency(),result,request.paymentMethod(),request.totalPrice()));
        //eventPublisher.publishClearCartEvent(mapper.toEvent(request));
        return ResponseEntity.ok(result);}
        catch(VariantNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        catch(VariantNotEnoughException e) {
            return ResponseEntity.badRequest().build();
        }
    } 

    @PutMapping("/update")
    public ResponseEntity<Void> updateOrder(@RequestBody UpdateOrderRequest request,@RequestHeader("X-User-Id") String userFromHeader, @RequestHeader("X-User-Roles") String role) {
        final var command = this.mapper.toCommand(request);
        try {
        this.updateService.update(command,userFromHeader,role);}
        catch(RuntimeException e) {
            e.printStackTrace();
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/cod_success") 
    public ResponseEntity<Void> codOrderSuccess(@RequestParam UUID orderId,@RequestHeader("X-User-Id") String userFromHeader, @RequestHeader("X-User-Roles") String role) {
        if(!checkPermission.isUser(role)) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            this.updateService.codOrderSuccess(new OrderId(orderId), userFromHeader);
        }
        catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.noContent().build();

    }


}
