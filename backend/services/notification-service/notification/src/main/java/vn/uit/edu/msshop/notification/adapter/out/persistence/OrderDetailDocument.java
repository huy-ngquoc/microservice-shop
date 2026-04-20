package vn.uit.edu.msshop.notification.adapter.out.persistence;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDocument {
    private UUID variantId;
    private String productName; 
    private int unitPrice;
    private int amount;
}
