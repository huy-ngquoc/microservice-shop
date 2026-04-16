package vn.uit.edu.msshop.inventory.adapter.out.redis;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.inventory.application.port.out.LoadInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.SyncInventoryPort;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryStatus;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Component

public class SyncInventoryAdapter implements SyncInventoryPort {
    private final LoadInventoryPort loadPort;
    private final RedisTemplate<String,Map<String,String>> redisTemplate;
    public SyncInventoryAdapter(@Lazy LoadInventoryPort loadPort, RedisTemplate<String,Map<String,String>> redisTemplate) {
        this.loadPort=loadPort;
        this.redisTemplate=redisTemplate;
    }
    @Override
    public Inventory loadFromMainDatabase(UUID variantId) {

        VariantId id = new VariantId(variantId);
        Inventory inventory = loadPort.loadByVariantIdAndStatus(id, new InventoryStatus("ENABLE")).orElse(null);
        if(inventory==null) return null;

       String key = "inventory:variant:" + variantId.toString();
       Map<String, String> stockData = new HashMap<>();
        stockData.put("quantity", String.valueOf(inventory.getQuantity().value()));
        stockData.put("reservedQuantity", String.valueOf(inventory.getReservedQuantity().value()));
        stockData.put("id",String.valueOf(inventory.getId().value()));
        stockData.put("status", String.valueOf(inventory.getStatus().value()));
        redisTemplate.opsForHash().putAll(key, stockData);
        
        redisTemplate.expire(key, Duration.ofDays(1));
        return inventory;
    }

}
