package vn.uit.edu.msshop.cart.adapter.in.web;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.adapter.in.web.mapper.CartWebMapper;
import vn.uit.edu.msshop.cart.adapter.in.web.request.CreateCartRequest;
import vn.uit.edu.msshop.cart.adapter.in.web.request.UpdateCartAmountReuest;
import vn.uit.edu.msshop.cart.adapter.in.web.response.CartResponse;
import vn.uit.edu.msshop.cart.application.dto.command.CreateCartCommand;
import vn.uit.edu.msshop.cart.application.dto.command.UpdateCartAmountCommand;
import vn.uit.edu.msshop.cart.application.dto.query.CartView;
import vn.uit.edu.msshop.cart.application.port.in.CheckPermissionUseCase;
import vn.uit.edu.msshop.cart.application.port.in.ClearCartUseCase;
import vn.uit.edu.msshop.cart.application.port.in.CreateCartUseCase;
import vn.uit.edu.msshop.cart.application.port.in.DeleteCartItemUseCase;
import vn.uit.edu.msshop.cart.application.port.in.GetCartUseCase;
import vn.uit.edu.msshop.cart.application.port.in.UpdateCartAmountUseCase;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final ClearCartUseCase clearUseCase;
    private final CreateCartUseCase createUseCase;
    private final DeleteCartItemUseCase deleteItemUseCase;

    private final UpdateCartAmountUseCase updateAmountUseCase;
    private final CartWebMapper cartWebMapper;
    private final GetCartUseCase getCartUseCase;
    private final CheckPermissionUseCase checkPermission;
    


    @GetMapping("/")
    public ResponseEntity<CartResponse> getCart(
            @RequestHeader("X-User-Id")
            String userFromHeader,
            @RequestHeader("X-User-Roles")
            String role) {
        System.out.println("Role " + role);
        if (!checkPermission.isUser(role)) {
            CartResponse response = null;
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        final var result = getCartUseCase.getByUserId(new UserId(UUID.fromString(userFromHeader)));
        return ResponseEntity.ok(cartWebMapper.toResponse(result));

    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(
            @RequestHeader("X-User-Id")
            String userFromHeader,
            @RequestHeader("X-User-Roles")
            String role) {
        if (!checkPermission.isUser(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clearUseCase.clear(cartWebMapper.toCommand(userFromHeader));
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/item")
    public ResponseEntity<Void> deleteCartItem(
            @RequestHeader("X-User-Id")
            String userFromHeader,
            @RequestHeader("X-User-Roles")
            String role,
            @RequestParam
            String variantId) {
        if (!checkPermission.isUser(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        deleteItemUseCase.deleteCartItem(cartWebMapper.toCommand(userFromHeader, variantId));
        return ResponseEntity.noContent().build();

    }

    @PutMapping("/")
    public ResponseEntity<CartResponse> updateCartItem(
            @RequestHeader("X-User-Id")
            String userFromHeader,
            @RequestHeader("X-User-Roles")
            String role,
            @RequestBody
            UpdateCartAmountReuest request) {
        if (!checkPermission.isUser(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UpdateCartAmountCommand command = cartWebMapper.toCommand(request,userFromHeader);
        
        CartView view = updateAmountUseCase.update(command);
        return ResponseEntity.ok(cartWebMapper.toResponse(view));
    }

    @PostMapping("/")
    public ResponseEntity<?> createCart(
            @RequestHeader("X-User-Id")
            String userFromHeader,
            @RequestHeader("X-User-Roles")
            String role,
            @RequestBody
            CreateCartRequest request) {
        /*if (!checkPermission.isUser(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }*/
        try {
        CreateCartCommand command = cartWebMapper.toCommand(request, userFromHeader);
        CartView view = createUseCase.create(command);
        return ResponseEntity.ok(cartWebMapper.toResponse(view));
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
