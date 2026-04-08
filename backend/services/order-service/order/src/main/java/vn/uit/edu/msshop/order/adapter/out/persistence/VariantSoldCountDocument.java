package vn.uit.edu.msshop.order.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection="variant_sold_count_document")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class VariantSoldCountDocument {
    @Id
    private UUID id;
    private int soldCount;
    @Version
    private Long version;
}
