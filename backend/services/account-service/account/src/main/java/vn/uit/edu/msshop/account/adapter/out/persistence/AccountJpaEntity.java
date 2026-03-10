package vn.uit.edu.msshop.account.adapter.out.persistence;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountJpaEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private String name;
    private String password;
    private String email;
    private String role;
    private String status;
    private String avatarUrl;
    private String avatarPublicId;
    private int avatarWidth;
    private int avatarHeight;
    private String shippingAddress;
    private String phoneNumber;
     @Version
    @Column(
            nullable = false)
    private long version;

    public static AccountJpaEntity of(UUID id, String name, String password, String email, String role, String status, String avatarUrl, String avatarPublicId, int avatarWidth, int avatarHeight, String shippingAddress, String phoneNumber) {
        return new AccountJpaEntity(id, name, password, email, role, status,avatarUrl,avatarPublicId,avatarWidth,avatarHeight,shippingAddress,phoneNumber,0L);
    }
    
    
}
