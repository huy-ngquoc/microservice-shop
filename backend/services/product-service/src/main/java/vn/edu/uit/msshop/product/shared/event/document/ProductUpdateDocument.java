package vn.edu.uit.msshop.product.shared.event.document;

import java.time.Instant;
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
@Document(collection="product_update_document")
public class ProductUpdateDocument {
    @Id
    private UUID eventId;
    private UUID productId;
    private String name;
    private String eventStatus;
    private Integer retryCount; 
    private Instant createdAt;
    private Instant updatedAt; 
    private String lastError;
}
