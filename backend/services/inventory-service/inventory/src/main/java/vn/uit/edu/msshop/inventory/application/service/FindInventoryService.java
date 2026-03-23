package vn.uit.edu.msshop.inventory.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;
import vn.uit.edu.msshop.inventory.application.exception.InventoryNotFoundException;
import vn.uit.edu.msshop.inventory.application.mapper.InventoryViewMapper;
import vn.uit.edu.msshop.inventory.application.port.in.FindInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.out.LoadInventoryPort;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class FindInventoryService implements FindInventoryUseCase{
    private final LoadInventoryPort loadPort;
    private final InventoryViewMapper mapper;
    @Override
    public InventoryView findById(InventoryId id) {
        final var result = loadPort.loadById(id).orElseThrow(()->new InventoryNotFoundException(id));
        return mapper.toView(result);
    }

    @Override
    public InventoryView findByVariantId(VariantId id) {
        final var result = loadPort.loadByVariantId(id).orElseThrow(()->new InventoryNotFoundException(id));
        return mapper.toView(result);
    }

    @Override
    public Page<InventoryView> findAll(int pageNumber, int pageSize) {
        final var result = loadPort.loadAll(pageNumber, pageSize);
        return result.map(mapper::toView);
    }

    @Override
    public List<InventoryView> findByListVariantId(List<VariantId> listVariantIds) {
        List<Inventory> listInventories = loadPort.findByListVariantId(listVariantIds);
        return listInventories.stream().map(mapper::toView).toList();
    }
     
}
