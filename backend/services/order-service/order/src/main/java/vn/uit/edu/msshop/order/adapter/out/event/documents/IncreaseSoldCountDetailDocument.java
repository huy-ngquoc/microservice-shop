package vn.uit.edu.msshop.order.adapter.out.event.documents;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncreaseSoldCountDetailDocument {
    private UUID variantId;
    private int amount;
}
