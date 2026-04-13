package vn.uit.edu.payment.adapter.out.persistence;

import java.time.Instant;
import java.util.UUID;

import org.jspecify.annotations.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(
        name = "payments")
@Getter
@Setter
@NoArgsConstructor(
        access = AccessLevel.PACKAGE)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class PaymentJpaEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private Instant createAt;
    private Instant updateAt;
    private String currency;
    private UUID orderId;
    private String paymentMethod;
    private String paymentStatus;
    private long paymentValue;
    private UUID userId;

     @Version
    @Column(
            nullable = false)
    private long version;

    public static @NonNull PaymentJpaEntity of(UUID id,

    Instant createAt,
    Instant updateAt,
    String currency,
    UUID orderId,
    String paymentMethod,
    String paymentStatus,
    long paymentValue,
    UUID userId
   ) {
    return new PaymentJpaEntity(id, createAt, updateAt, currency, orderId, paymentMethod, paymentStatus, paymentValue,userId, 0L);
   }

}
