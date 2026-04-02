package vn.uit.edu.msshop.account.adapter.out.event.documents;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection="outbox_event_rollback_image_event")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RollbackImageEventDocument {
    @Id
    private UUID eventId;
    private String imagePublicId;
    private String eventStatus;
    private Integer retryCount; 
    private Instant createdAt;
    private Instant updatedAt; 
    private String lastError;
    

}
