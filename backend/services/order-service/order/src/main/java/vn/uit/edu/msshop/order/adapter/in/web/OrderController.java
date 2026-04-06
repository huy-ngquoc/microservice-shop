package vn.uit.edu.msshop.order.adapter.in.web;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.exception.VariantNotEnoughException;
import vn.uit.edu.msshop.order.adapter.exception.VariantNotFoundException;
import vn.uit.edu.msshop.order.adapter.in.web.mapper.OrderWebMapper;
import vn.uit.edu.msshop.order.adapter.in.web.request.CreateOrderRequest;
import vn.uit.edu.msshop.order.adapter.in.web.request.UpdateOrderRequest;
import vn.uit.edu.msshop.order.adapter.in.web.response.OrderResponse;
import vn.uit.edu.msshop.order.application.port.in.CheckPermissionUseCase;
import vn.uit.edu.msshop.order.application.port.in.CreateOrderUseCase;
import vn.uit.edu.msshop.order.application.port.in.DeleteOrderUseCase;
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
    private final Tracer tracer;
    private final DeleteOrderUseCase deleteOrderUseCase;
    @GetMapping("/test-trace")
    public String test() {
        
        var currentSpan = tracer.currentSpan();

    // 2. Nếu null (chưa có span tự động), hãy tự tạo một span "mồi"
    if (currentSpan == null) {
        // Tạo một span mới, đặt tên là "manual-test"
        var newSpan = tracer.nextSpan().name("manual-test-span").start();
        
        // Dùng try-with-resources để đưa span này vào ngữ cảnh (Scope)
        try (var ws = tracer.withSpan(newSpan)) {
            String traceId = newSpan.context().traceId();
            return "New Trace ID (Manual): " + traceId;
        } finally {
            newSpan.end(); // Kết thúc span để nó gửi dữ liệu sang Zipkin
        }
    }

    // 3. Nếu đã có span tự động (thông thường là vậy)
    return "Current Trace ID: " + currentSpan.context().traceId();
    }

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
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearDatabase() {
        deleteOrderUseCase.deleteAll();
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getAll() {
        var result = findService.findAll();
        return ResponseEntity.ok(result.stream().map(mapper::toResponse).toList());
    }


}
