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

     @Version
    @Column(
            nullable = false)
    private long version;

    public static AccountJpaEntity of(UUID id, String name, String password, String email, String role, String status) {
        return new AccountJpaEntity(id, name, password, email, role, status,0L);
    }
    
    
}
