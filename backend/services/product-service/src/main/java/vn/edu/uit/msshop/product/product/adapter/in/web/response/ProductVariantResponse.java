package vn.edu.uit.msshop.product.product.adapter.in.web.response;

import java.util.List;
import java.util.UUID;

public record ProductVariantResponse(UUID id,long price,List<String>traits){public ProductVariantResponse{traits=List.copyOf(traits);}}
