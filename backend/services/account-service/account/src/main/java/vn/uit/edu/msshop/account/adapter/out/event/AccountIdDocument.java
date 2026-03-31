package vn.uit.edu.msshop.account.adapter.out.event;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountIdDocument {
    @Id
    private UUID eventId;
    private UUID accontId;
    private String eventStatus;
    private Integer retryCount; 
    private Instant createdAt;
    private Instant updatedAt; 
    private String lastError;
}
