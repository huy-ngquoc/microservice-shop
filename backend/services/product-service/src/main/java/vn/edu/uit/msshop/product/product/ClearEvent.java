package vn.edu.uit.msshop.product.product;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.shared.event.repository.ProductCreatedRepository;
import vn.edu.uit.msshop.product.shared.event.repository.ProductDeletedRepository;
import vn.edu.uit.msshop.product.shared.event.repository.ProductUpdateRepository;
import vn.edu.uit.msshop.product.shared.event.repository.VariantDeletedRepository;
import vn.edu.uit.msshop.product.shared.event.repository.VariantUpdateRepository;

@RestController
@RequiredArgsConstructor
public class ClearEvent {
  private final ProductCreatedRepository productCreatedRepo;
  private final ProductUpdateRepository productUpdateRepo;
  private final ProductDeletedRepository productDeletedRepo;
  private final VariantUpdateRepository variantUpdateRepo;
  private final VariantDeletedRepository variantDeletedRepo;

  @DeleteMapping("/clear")
  public ResponseEntity<Void> clear() {
    productCreatedRepo.deleteAll();
    productUpdateRepo.deleteAll();
    productDeletedRepo.deleteAll();
    variantUpdateRepo.deleteAll();
    variantDeletedRepo.deleteAll();
    return ResponseEntity.noContent().build();
  }
}
