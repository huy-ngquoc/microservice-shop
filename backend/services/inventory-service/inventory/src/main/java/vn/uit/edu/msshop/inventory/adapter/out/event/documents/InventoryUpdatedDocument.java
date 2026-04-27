package vn.uit.edu.msshop.inventory.adapter.out.event.documents;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="inventory_updated")
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
