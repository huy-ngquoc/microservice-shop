package vn.uit.edu.payment.adapter.in.web;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.adapter.in.web.mapper.PaymentWebMapper;
import vn.uit.edu.payment.adapter.in.web.request.CreatePaymentRequest;
import vn.uit.edu.payment.adapter.in.web.request.UpdatePaymentRequest;
import vn.uit.edu.payment.adapter.in.web.response.PaymentResponse;
import vn.uit.edu.payment.application.port.in.CreatePaymentUseCase;
import vn.uit.edu.payment.application.port.in.LoadPaymentUseCase;
import vn.uit.edu.payment.application.port.in.UpdatePaymentUseCase;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final CreatePaymentUseCase createUseCase;
    private final UpdatePaymentUseCase updateUseCase;
    private final LoadPaymentUseCase loadPaymentUseCase;
    private final PaymentWebMapper mapper;

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable UUID paymentId) {
        final var result = mapper.toResponse(loadPaymentUseCase.findById(new PaymentId(paymentId)));
        return ResponseEntity.ok(result);
    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable UUID orderId) {
        final var result = mapper.toResponse(loadPaymentUseCase.loadByOrderId(new OrderId(orderId)));
        return ResponseEntity.ok(result);
    }
    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody CreatePaymentRequest request) {
        final var command = mapper.toCommand(request);
        final var result =createUseCase.create(command);
        return ResponseEntity.ok(mapper.toResponse(result));
    } 
    @PutMapping("/update")
    public ResponseEntity<PaymentResponse> updatePayment(@RequestBody UpdatePaymentRequest request) {
        final var command = mapper.toCommand(request);
        final var result =updateUseCase.update(command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }
}
