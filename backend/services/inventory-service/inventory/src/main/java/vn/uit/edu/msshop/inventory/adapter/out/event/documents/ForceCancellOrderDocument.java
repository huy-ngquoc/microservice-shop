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


@Getter
@Setter
@Builder
@Entity
@Table(name="force_cancell_order")
@AllArgsConstructor
@NoArgsConstructor
public class ForceCancellOrderDocument {
    @Id
    private UUID eventId;
    private UUID orderId;
    private String eventStatus;
    private Integer retryCount; 
    private Instant createdAt;
    private Instant updatedAt; 
    private String lastError;

}
