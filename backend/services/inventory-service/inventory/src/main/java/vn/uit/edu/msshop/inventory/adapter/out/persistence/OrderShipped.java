package vn.uit.edu.msshop.inventory.adapter.out.persistence;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="order-shipped")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderShipped {
    @Id
    private UUID orderId;
    private Instant receiveAt;
}
