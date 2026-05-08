package vn.uit.edu.msshop.order.adapter.out.event.documents;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class OutboxEvent {
    protected String eventStatus;
    protected Integer retryCount; 
    protected Instant createdAt;
    protected Instant updatedAt; 
    protected String lastError;
}
