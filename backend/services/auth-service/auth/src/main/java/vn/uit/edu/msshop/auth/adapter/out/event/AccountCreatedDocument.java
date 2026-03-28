package vn.uit.edu.msshop.auth.adapter.out.event;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Document(collection="outbox_events")
@Getter
@Setter
@Builder
public class AccountCreatedDocument {
    @Id
    private UUID eventId;
    private UUID accountId;
    private String name; 
    private String email;
    private String password;
    private String role;
    private String status;
    private String shippingAddress;
    private String phoneNumber;
    private String eventStatus;
}
