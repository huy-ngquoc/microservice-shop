package vn.uit.edu.msshop.account.adapter.out.persistence;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="account_outbox")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountOutboxEntity {
    @Id
    private UUID userId;
    @OneToOne
    @MapsId
    @JoinColumn(name="user_id")
    private AccountJpaEntity account;
    private String userName;
    private String password;
    private String userRole;
    private String userEmail;
    private String firstName;
    private String lastName;
    private boolean isCheck;
    private Instant createdAt;
    private Instant updatedAt;
    private int retryCount;
    private String lastError;

    public void handleSuccess() {
        this.isCheck=true;
    }
    public void handleFailure(String error) {
        this.lastError=error;
        this.retryCount++;
    }

    
}
