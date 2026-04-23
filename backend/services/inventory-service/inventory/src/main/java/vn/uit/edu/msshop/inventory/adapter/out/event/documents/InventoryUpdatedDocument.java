package vn.uit.edu.msshop.inventory.adapter.out.event.documents;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Document(collection="outbox_events_inventory_update")
@Getter
@Setter
@Builder
public class InventoryUpdatedDocument {
    @Id
    private UUID eventId;
    private UUID variantId;
    private int newQuantity;
    private int newReservedQuantity;
    private String eventStatus;
    private Integer retryCount; 
    private Instant createdAt;
    private Instant updatedAt; 
    private String lastError;
    private boolean isRead;
    

    
}
