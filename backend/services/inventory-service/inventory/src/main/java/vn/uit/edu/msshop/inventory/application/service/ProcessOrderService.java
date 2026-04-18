package vn.uit.edu.msshop.inventory.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.application.exception.InsufficientStockException;
import vn.uit.edu.msshop.inventory.application.exception.InventoryNotFoundException;
import vn.uit.edu.msshop.inventory.application.port.in.ProcessOrderUseCase;
import vn.uit.edu.msshop.inventory.application.port.out.LoadFromRedisPort;
import vn.uit.edu.msshop.inventory.application.port.out.SyncInventoryPort;
import vn.uit.edu.msshop.inventory.config.RedisConfig;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.OrderDetail;
import vn.uit.edu.msshop.inventory.domain.model.OrderProcessJob;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class ProcessOrderService implements ProcessOrderUseCase {
    private final LoadFromRedisPort loadFromRedisPort;
    private final RedisTemplate<String,Map<String,String>> redisTemplate;
    private final RedisConfig redisConfig;
    private final SyncInventoryPort syncPort;
    
    @Override
    public void processOrder(List<OrderDetail> orderDetails) {
        List<VariantId> ids = orderDetails.stream().map(item->item.getVariantId()).toList();
        List<Inventory> inventories = loadFromRedisPort.loadFromRedis(ids);
        List<OrderProcessJob> orderProcessJobs = new ArrayList<>();
        for(OrderDetail o:orderDetails) {
            Inventory i = findInList(inventories, o.getVariantId());
            if(i==null) throw new InventoryNotFoundException(o.getVariantId());
            if(i.getQuantity().value()<o.getQuantity().value()) throw new InsufficientStockException(o.getVariantId());
            orderProcessJobs.add(new OrderProcessJob(i,o.getQuantity()));
        }
        for(OrderProcessJob job:orderProcessJobs) {
            processScript(job.getInventory().getVariantId().value(), job.getQuantity().value(), redisConfig.getReserveStockScript());
        }

    }
    private Inventory findInList(List<Inventory> inventories, VariantId variantId) {
        for(Inventory i: inventories) {
            if(i.getVariantId().value().equals(variantId.value())) return i;
        }
        return null;
    }
    public void processScript(UUID variantId, int amount, DefaultRedisScript<Long> script) {
        String key = "inventory:variant:" + variantId.toString();
    
    Long result = redisTemplate.execute(script, List.of(key), String.valueOf(amount));

    if (result == -1) {
        // Tình huống: Redis trống trơn (hết hạn hoặc chưa nạp)
        //System.out.println("Redis miss! Loading from DB...");
        Inventory inventory = syncPort.loadFromMainDatabase(variantId);
        if(inventory==null) throw new InventoryNotFoundException(new VariantId(variantId));
        // Sau khi load xong thì gọi lại chính nó (Recursive) hoặc báo user thử lại
        processScript(variantId, amount, script); 
    } else if (result == 0) {
        throw new InsufficientStockException(new VariantId(variantId));
    } else if (result == -2) {
        throw new IllegalStateException("Dữ liệu tồn kho trên Redis bị lỗi định dạng!");
    } else if (result == 1) {
        
    }
    }
    
}
