package vn.uit.edu.msshop.inventory.application.service;

import java.time.Instant;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;
import vn.uit.edu.msshop.inventory.application.exception.InventoryNotFoundException;
import vn.uit.edu.msshop.inventory.application.mapper.InventoryViewMapper;
import vn.uit.edu.msshop.inventory.application.port.in.FindInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.out.LoadInventoryPort;
import vn.uit.edu.msshop.inventory.bootstrap.config.cache.CacheNames;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class FindInventoryService implements FindInventoryUseCase {
    private final LoadInventoryPort loadPort;
    private final InventoryViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.INVENTORY,
            key = "#id.value()")
    public InventoryView findById(
            InventoryId id) {
        final var result = loadPort.loadById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));
        return mapper.toView(result);
    }

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.INVENTORY_BY_VARIANT,
            key = "#id.value()")
    public InventoryView findByVariantId(
            VariantId id) {
        final var result = loadPort.loadByVariantId(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));
        return mapper.toView(result);
    }

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.INVENTORY_LIST,
            key = "#pageNumber + ':' + #pageSize")
    public Page<InventoryView> findAll(
            int pageNumber,
            int pageSize) {
        final var result = loadPort.loadAll(pageNumber, pageSize);
        return result.map(mapper::toView);
    }

    @Override
    @Transactional(
            readOnly = true)
    public List<InventoryView> findByListVariantId(
            List<VariantId> listVariantIds) {
        List<Inventory> listInventories = loadPort.findByListVariantId(listVariantIds);
        return listInventories.stream().map(mapper::toView).toList();
    }

    @Override
    @Transactional(
            readOnly = true)
    public Page<InventoryView> findAllUpdatedInventory(
            Instant startFirst,
            Instant endFirst,
            Instant startSecond,
            Instant endSecond,
            Pageable pageable) {
        Page<Inventory> result = loadPort.findAllUpdatedInventory(startFirst, endFirst, startSecond, endSecond,
                pageable);
        return result.map(mapper::toView);
    }

}
