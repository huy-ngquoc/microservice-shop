package vn.uit.edu.msshop.recommendation.domain.event;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateVariantEvent {
    private UUID variantId;
    private List<String> images;
    private List<String> targets;
    private String name;
}
