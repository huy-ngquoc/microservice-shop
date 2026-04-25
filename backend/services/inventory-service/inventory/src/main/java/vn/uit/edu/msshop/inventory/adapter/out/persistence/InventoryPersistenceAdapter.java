package vn.uit.edu.msshop.inventory.adapter.out.persistence;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.adapter.out.persistence.mapper.InventoryJpaMapper;
import vn.uit.edu.msshop.inventory.application.exception.InventoryNotFoundException;
import vn.uit.edu.msshop.inventory.application.port.out.DeleteInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.DeleteRedisPort;
import vn.uit.edu.msshop.inventory.application.port.out.LoadFromRedisPort;
import vn.uit.edu.msshop.inventory.application.port.out.LoadInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.SaveInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.SyncInventoryPort;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryStatus;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.ReservedQuantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Component
@RequiredArgsConstructor
public class InventoryPersistenceAdapter implements LoadInventoryPort, SaveInventoryPort, DeleteInventoryPort, LoadFromRedisPort,DeleteRedisPort {
    private final InventoryJpaMapper mapper;
    private final SpringDataInventoryJpaRepository repository;
    private final RedisTemplate<String,Map<String,String>> redisTemplate;
    private final SyncInventoryPort syncInventoryPort;

    @Override
    @Transactional
    public Optional<Inventory> loadById(InventoryId id) {
        Optional<InventoryJpaEntity> result = repository.findById(id.value());
        if(result.isEmpty()) return Optional.empty();
        return Optional.of(mapper.toDomain(result.get()));
    }

    @Override
    @Transactional
    public Optional<Inventory> loadByVariantId(VariantId id) {
        Optional<InventoryJpaEntity> result = repository.findByVariantId(id.value());
        if(result.isEmpty()) return Optional.empty();
        return Optional.of(mapper.toDomain(result.get()));
    }

    @Override
    @Transactional
    public Page<Inventory> loadAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<InventoryJpaEntity> result = repository.findAll(pageable);
        return result.map(mapper::toDomain);
    }

    @Override
    @Transactional
    public Inventory save(Inventory inventory) {
        InventoryJpaEntity toSave = mapper.toEntity(inventory);
        InventoryJpaEntity result = repository.save(toSave);
        return mapper.toDomain(result);
    }

    @Override
    @Transactional
    public Inventory createNew(VariantId variantId) {
        InventoryJpaEntity newEntity = mapper.toNew(variantId);
        InventoryJpaEntity result = repository.save(newEntity);
        return mapper.toDomain(result);
    }

    @Override
    @Transactional
    public List<Inventory> findByListVariantId(List<VariantId> variantIds) {
        List<InventoryJpaEntity> result = repository.findByVariantIdIn(variantIds.stream().map(item->item.value()).toList());
        return result.stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public List<Inventory> saveAll(List<Inventory> inventories) {
        List<InventoryJpaEntity> result = repository.saveAll(inventories.stream().map(mapper::toEntity).toList());
        return result.stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public Inventory createFromCommand(VariantId variantId, Quantity quantity) {
        InventoryJpaEntity newEntity = mapper.toNewFromCommand(variantId, quantity);
        InventoryJpaEntity result = repository.save(newEntity);
        return mapper.toDomain(result);
    }

    @Override
    public void delete(Inventory inventory) {
        InventoryJpaEntity jpaEntity = mapper.toEntity(inventory);
        repository.delete(jpaEntity);
    }

    @Override
    @Transactional
    public List<Inventory> loadFromRedis(List<VariantId> variantIds) {
        List<Inventory> result = new ArrayList<>();
        try {
    for (VariantId variantId : variantIds) {
        String key = "inventory:variant:" + variantId.value().toString();
        
        // 1. Dùng entries() để lấy toàn bộ Hash thay vì get() của String
        Map<Object, Object> val = redisTemplate.opsForHash().entries(key);

        // 2. Với Hash, nếu không tồn tại, Redis trả về Map trống chứ không phải null
        if (val == null || val.isEmpty()) {
            Inventory inventory = syncInventoryPort.loadFromMainDatabase(variantId.value());
            if (inventory == null) throw new InventoryNotFoundException(variantId);
            result.add(inventory);
            continue;
        }

        // 3. Ép kiểu và lấy dữ liệu (Vì opsForHash trả về Map<Object, Object>)
        int quantity = Integer.parseInt(val.get("quantity").toString());
        int reservedQuantity = Integer.parseInt(val.get("reservedQuantity").toString());
        String inventoryStatus = val.get("status").toString();
        
        // Tạo domain object thông qua Mapper hoặc constructor
        var snapshot = Inventory.Snapshot.builder().id(new InventoryId(UUID.fromString(val.get("id").toString())))
        .quantity(new Quantity(quantity))
        .variantId(variantId)
        .reservedQuantity(new ReservedQuantity(reservedQuantity))
        .status(new InventoryStatus(inventoryStatus))
        .build();
        result.add(Inventory.reconstitue(snapshot));
    }
    return result;
}catch(Exception e) {
    return findByListVariantId(variantIds);
}
        
    }

    @Override
    public Optional<Inventory> loadByVariantIdAndStatus(VariantId id, InventoryStatus status) {
        Optional<InventoryJpaEntity> inventoryJpaEntity = repository.findByVariantIdAndStatus(id.value(), status.value());
        if(inventoryJpaEntity.isEmpty()) return Optional.empty();
        return Optional.of(mapper.toDomain(inventoryJpaEntity.get()));
    }

    @Override
    public List<Inventory> createNews(List<VariantId> variantIds) {
        List<InventoryJpaEntity> inventories = variantIds.stream().map(item->mapper.toNew(item)).toList();
        List<InventoryJpaEntity> results = repository.saveAll(inventories);
        return results.stream().map(mapper::toDomain).toList();
    }

    @Override
    public Inventory loadById(VariantId variantId) {
        String key = "inventory:variant:" + variantId.value().toString();
        try{
        
        // 1. Dùng entries() để lấy toàn bộ Hash thay vì get() của String
        Map<Object, Object> val = redisTemplate.opsForHash().entries(key);

        
        if (val == null || val.isEmpty()) {
            Inventory inventory = syncInventoryPort.loadFromMainDatabase(variantId.value());
            if (inventory == null) return null;
            return inventory;
        }

        // 3. Ép kiểu và lấy dữ liệu (Vì opsForHash trả về Map<Object, Object>)
        int quantity = Integer.parseInt(val.get("quantity").toString());
        int reservedQuantity = Integer.parseInt(val.get("reservedQuantity").toString());
        String inventoryStatus = val.get("status").toString();
        
        // Tạo domain object thông qua Mapper hoặc constructor
        var snapshot = Inventory.Snapshot.builder().id(new InventoryId(UUID.randomUUID()))
        .quantity(new Quantity(quantity))
        .variantId(variantId)
        .reservedQuantity(new ReservedQuantity(reservedQuantity))
        .status(new InventoryStatus(inventoryStatus))
        .build();
        return Inventory.reconstitue(snapshot);
    }
    catch(Exception e) {
        return loadByVariantId(variantId).orElse(null);
    }
    }

    @Override
    public void delete(VariantId variantId) {
        String key = "inventory:variant:" + variantId.value().toString();
    
    try {
    redisTemplate.delete(key);
    
    }
    catch(Exception e) {

    }
    finally {
        repository.deleteById(variantId.value());
    }
    }

    @Override
    public List<Inventory> saveFromSet(Set<Inventory> inventories) {
        List<InventoryJpaEntity> listInventories = inventories.stream().map(mapper::toEntity).toList();
        return repository.saveAll(listInventories).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Inventory saveToRedis(Inventory inventory) {
         String key = "inventory:variant:" + inventory.getVariantId().toString();
       Map<String, String> stockData = new HashMap<>();
        stockData.put("quantity", String.valueOf(inventory.getQuantity().value()));
        stockData.put("reservedQuantity", String.valueOf(inventory.getReservedQuantity().value()));
        stockData.put("id",String.valueOf(inventory.getId().value()));
        stockData.put("status", String.valueOf(inventory.getStatus().value()));
        redisTemplate.opsForHash().putAll(key, stockData);
        
        redisTemplate.expire(key, Duration.ofDays(1));
        return inventory;
    }

    @Override
    public List<Inventory> saveToRedis(List<Inventory> inventories) {
        return inventories.stream().map(this::saveToRedis).toList();

    }
}
