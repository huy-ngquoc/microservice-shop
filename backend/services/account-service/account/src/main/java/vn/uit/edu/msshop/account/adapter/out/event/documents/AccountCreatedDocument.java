package vn.uit.edu.msshop.account.adapter.out.event.documents;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="account_created")
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
