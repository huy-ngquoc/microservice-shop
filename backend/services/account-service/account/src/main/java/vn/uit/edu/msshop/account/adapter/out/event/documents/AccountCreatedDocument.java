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

@Document(collection="outbox_events_account_created")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountCreatedDocument {
    @Id
    private UUID eventId;
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String status;
    private String shippingAddress;
    private String phoneNumber;
    private String eventStatus;
    private Integer retryCount; 
    private Instant createdAt;
    private Instant updatedAt; 
    private String lastError;
    
}
