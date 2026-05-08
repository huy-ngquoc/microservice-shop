package vn.uit.edu.msshop.inventory.application.port.in;

import java.util.List;
import java.util.UUID;

import vn.uit.edu.msshop.inventory.adapter.in.web.response.InventoryResponse;

public interface LoadRedisInventoryPort {
    public List<InventoryResponse> getResponses(List<UUID> variantIds);
        
    
}
