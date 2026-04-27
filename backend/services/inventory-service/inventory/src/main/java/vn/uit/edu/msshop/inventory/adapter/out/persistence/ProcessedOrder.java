package vn.uit.edu.msshop.inventory.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="processed_order") 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedOrder {
    @Id
    private UUID outboxId;
    private String outboxStatus;
}
