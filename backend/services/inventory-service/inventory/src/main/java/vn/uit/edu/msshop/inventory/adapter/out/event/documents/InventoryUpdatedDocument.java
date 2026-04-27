package vn.uit.edu.msshop.inventory.adapter.out.event.documents;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="inventory_updated")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
