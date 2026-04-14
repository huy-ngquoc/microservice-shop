package vn.edu.uit.msshop.product.shared.event.document;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection="product_deleted_document")
public class ProductDeletedDocument {
    @Id
    private UUID eventId;
    private UUID productId;
    private String eventStatus;
    private Integer retryCount; 
    private Instant createdAt;
    @Nullable
    private Instant updatedAt; 
    @Nullable
    private String lastError;
    
}
