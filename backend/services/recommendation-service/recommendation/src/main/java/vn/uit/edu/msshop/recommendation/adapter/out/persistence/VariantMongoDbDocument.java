package vn.uit.edu.msshop.recommendation.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "variants")
public class VariantMongoDbDocument {
    @Id
    private UUID id;

    private String name;
    private List<String> targets;
    private List<String> images;
}
