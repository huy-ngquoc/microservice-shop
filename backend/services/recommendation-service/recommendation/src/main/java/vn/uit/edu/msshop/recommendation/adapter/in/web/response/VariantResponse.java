package vn.uit.edu.msshop.recommendation.adapter.in.web.response;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VariantResponse {
    private UUID variantId;
    private List<String> images;
    private List<String> targets;
    private String name;
}
