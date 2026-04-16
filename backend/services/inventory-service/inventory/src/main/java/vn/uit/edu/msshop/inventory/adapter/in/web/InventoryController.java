package vn.uit.edu.msshop.inventory.adapter.in.web;

import java.util.List;
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
import vn.uit.edu.msshop.inventory.adapter.in.web.mapper.InventoryWebMapper;
import vn.uit.edu.msshop.inventory.adapter.in.web.request.CreateInventoryRequest;
import vn.uit.edu.msshop.inventory.adapter.in.web.request.UpdateInventoryRequest;
import vn.uit.edu.msshop.inventory.adapter.in.web.response.InventoryResponse;
import vn.uit.edu.msshop.inventory.adapter.out.redis.InventorySyncJob;
import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;
import vn.uit.edu.msshop.inventory.application.port.in.CheckPermissionUseCase;
import vn.uit.edu.msshop.inventory.application.port.in.CreateInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.in.FindInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.in.UpdateInventoryUseCase;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryWebMapper mapper;
    private final FindInventoryUseCase findUseCase;
    private final UpdateInventoryUseCase updateUseCase;
    private final CheckPermissionUseCase checkPermission;
    private final CreateInventoryUseCase createUseCase;
    private final InventorySyncJob syncJob;
    @GetMapping("/")
    public ResponseEntity<Page<InventoryResponse>> getAll(@RequestHeader("X-User-Id") String userFromHeader, @RequestHeader("X-User-Roles") String role, @RequestParam(defaultValue="7") int pageSize, @RequestParam(defaultValue="0") int pageNumber) {
        if(!checkPermission.isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Page<InventoryView> result = findUseCase.findAll(pageNumber, pageSize);
        Page<InventoryResponse> response = result.map(mapper::toResponse);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/sync")
    public String sync() {
        syncJob.syncRedisToDb();
        return "Success";
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getById(@RequestHeader("X-User-Id") String userFromHeader, @RequestHeader("X-User-Roles") String role, @PathVariable UUID id) {
        if(!checkPermission.isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        InventoryView view = findUseCase.findById(new InventoryId(id));
        return ResponseEntity.ok(mapper.toResponse(view));
    }
    @GetMapping("/variant/{id}")
    public ResponseEntity<InventoryResponse> getByVariantId(@RequestHeader("X-User-Id") String userFromHeader, @RequestHeader("X-User-Roles") String role, @PathVariable UUID id) {
        if(!checkPermission.isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        InventoryView view = findUseCase.findByVariantId(new VariantId(id));
        return ResponseEntity.ok(mapper.toResponse(view));
    }
    @GetMapping("/public/variant/{id}")
    public ResponseEntity<InventoryResponse> getByVariantId(@PathVariable UUID id) {
        InventoryView view = findUseCase.findByVariantId(new VariantId(id));
        return ResponseEntity.ok(mapper.toResponse(view));
    }
    @PostMapping("/public/variants")
    public ResponseEntity<List<InventoryResponse>> getByVariantIds(@RequestBody List<UUID> variantIds) {
        System.out.println(variantIds.size());
        List<VariantId> listIds = variantIds.stream().map(item->new VariantId(item)).toList();
        List<InventoryView> views = findUseCase.findByListVariantIdFromRedis(listIds);
        return ResponseEntity.ok(views.stream().map(mapper::toResponse).toList());
    }
    @PutMapping("/")
    public ResponseEntity<InventoryResponse> update(@RequestHeader("X-User-Id") String userFromHeader, @RequestHeader("X-User-Roles") String role, @RequestBody UpdateInventoryRequest request) {
        if(!checkPermission.isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        final var command = mapper.toCommand(request);
        InventoryView result = updateUseCase.update(command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }
    @PostMapping("/")
    public ResponseEntity<InventoryResponse> create(@RequestHeader("X-User-Id") String userFromHeader, @RequestHeader("X-User-Roles") String role, @RequestBody CreateInventoryRequest request) {
        System.out.println("Roleeee " +role);
         if(!checkPermission.isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        final var command = mapper.toCommand(request);
        InventoryView result = createUseCase.create(command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }



}
