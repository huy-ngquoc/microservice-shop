package vn.edu.uit.msshop.product.shared.event.document;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection="variant_update_document")
public class VariantUpdateDocument  {
    @Id
    private UUID eventId;
    private UUID variantId;
    private UUID productId;
    private String productName;
    private long price;
    private List<String> traits;
    private String imageKey;
    private String eventStatus;
    private Integer retryCount; 
    private Instant createdAt;
    private Instant updatedAt; 
    private String lastError;
    

}
