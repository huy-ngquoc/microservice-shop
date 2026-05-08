package vn.uit.edu.msshop.order.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="variant_info")
public class VariantInfo {
        @Id
        private UUID variantId;

        private UUID productId;

        private String productName;

        private long price;

        

        private List<String> traits;

        
        private String imageKey;
}
