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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection="variant_purge_document")
public class VariantPurgeDocument {
    @Id
    private UUID eventId;
    private UUID variantId;
    private String eventStatus;
    private Integer retryCount; 
    private Instant createdAt;
    @Nullable
    private Instant updatedAt;
    @Nullable 
    private String lastError;

}