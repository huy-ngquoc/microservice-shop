package vn.uit.edu.msshop.rating.adapter.out.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.application.port.out.CheckProductPort;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
@Component
@RequiredArgsConstructor
public class CheckProductAdapter implements CheckProductPort{
    private final CheckProductFeign checkProductFeign;
    @Override
    public boolean isProductExist(ProductId productId) {
        try {
            ResponseEntity<Void> response = checkProductFeign.existsById(productId.value());
            
            return response.getStatusCode().is2xxSuccessful();
        } catch (FeignException.NotFound e) {
            
            return false;
        } catch (Exception e) {
            
            return false; 
        }
    }

}
