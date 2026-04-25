package vn.uit.edu.payment.adapter.out.persistence;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="payback_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaybackPayments {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    private UUID userId;
    private long value;
    
}
