package vn.edu.uit.msshop.product.product.adapter.in.web;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.adapter.in.web.mapper.ProductWebMapper;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductActiveExistenceCheckByIdUseCase;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductInternalController {
    private final ProductActiveExistenceCheckByIdUseCase activeExistenceCheckByIdUseCase;
    private final ProductWebMapper mapper;

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> existsActiveById(
            @PathVariable
            final UUID id) {
        final var query = this.mapper.toActiveExistenceCheckByIdQuery(id);
        final var existed = this.activeExistenceCheckByIdUseCase.exists(query);
        if (!existed) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
