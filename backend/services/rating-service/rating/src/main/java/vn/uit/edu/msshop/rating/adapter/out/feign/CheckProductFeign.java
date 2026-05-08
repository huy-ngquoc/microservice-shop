package vn.uit.edu.msshop.rating.adapter.out.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name="product-service")
public interface CheckProductFeign {
    @GetMapping("/products/{id}/exists")
    public ResponseEntity<Void> existsById(
            @PathVariable
            final UUID id);
}
