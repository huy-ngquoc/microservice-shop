package vn.uit.edu.msshop.inventory.adapter.out.event.documents;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@Entity
@Table(name="force_cancell_order")
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
