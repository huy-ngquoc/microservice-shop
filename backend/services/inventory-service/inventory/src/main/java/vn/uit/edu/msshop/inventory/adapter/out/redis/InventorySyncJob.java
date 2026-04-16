package vn.uit.edu.msshop.inventory.adapter.out.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.application.port.out.LoadInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.SaveInventoryPort;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.ReservedQuantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class InventorySyncJob {

    private final RedisTemplate<String, Map<String, String>> redisMapTemplate;
    
    private final LoadInventoryPort loadInventoryPort;
    private final SaveInventoryPort saveInventoryPort; 

    
    @Scheduled(fixedRate = 1800000) 
    public void syncRedisToDb() {
        
        ScanOptions options = ScanOptions.scanOptions()
                .match("inventory:variant:*")
                .count(100) 
                .build();

        try (Cursor<byte[]> cursor = redisMapTemplate.getConnectionFactory()
                .getConnection().scan(options)) {

            List<Inventory> updateList = new ArrayList<>();

            while (cursor.hasNext()) {
                String key = new String(cursor.next());
                
                
                Map<Object, Object> entries = redisMapTemplate.opsForHash().entries(key);
                
                if (!entries.isEmpty()) {
                    
                    UUID variantId = UUID.fromString(key.replace("inventory:variant:", ""));
                    int quantity = Integer.parseInt(entries.get("quantity").toString());
                    int reservedQuantity = Integer.parseInt(entries.get("reservedQuantity").toString());
                    String status = entries.get("status").toString();
                    UUID id = UUID.fromString(entries.get("id").toString());
                    
                    Inventory inventory = loadInventoryPort.loadByVariantId(new VariantId(variantId)).orElse(null);
                    if(inventory!=null) {
                        
                        final var updateInfo = Inventory.UpdateInfo.builder().inventoryId(inventory.getId())
                        
                        .quantity(new Quantity(quantity))
                        .reservedQuantity(new ReservedQuantity(reservedQuantity)).build();
                        final var toSave = inventory.applyUpdateInfo(updateInfo);
                        if(status.equals("ENABLE")) toSave.restore();
                        else toSave.disable();
                        updateList.add(toSave);
                    }
                    else {
                        System.out.println("Inventory is null with id "+variantId);
                        final var draft = Inventory.Draft.builder().id(new InventoryId(id))
                        .variantId(new VariantId(variantId))
                        .quantity(new Quantity(quantity))
                        .reservedQuantity(new ReservedQuantity(reservedQuantity))
                        .build();
                        updateList.add(Inventory.create(draft));

                    }
                }

                
                if (updateList.size() >= 50) {
                    saveInventoryPort.saveAll(updateList);
                    updateList.clear();
                }
            }
            
            
            if (!updateList.isEmpty()) {
                System.out.println("Size of list "+updateList.size());
                saveInventoryPort.saveAll(updateList);
            }
        }
        System.out.println("Inventory Sync Completed!");
    }

    @Scheduled(fixedRate = 1800000) 
    public void clearRedis() {
        ScanOptions options = ScanOptions.scanOptions()
                    .match("inventory:variant:*")
                    .build();

            try (Cursor<byte[]> cursor = redisMapTemplate.getConnectionFactory()
                    .getConnection().scan(options)) {
                
                while (cursor.hasNext()) {
                    redisMapTemplate.delete(new String(cursor.next()));
                }
            }
    }
}
