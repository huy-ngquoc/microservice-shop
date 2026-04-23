package vn.uit.edu.msshop.inventory.adapter.out.persistence;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.adapter.out.event.documents.InventoryUpdatedDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.InventoryUpdatedDocumentRepository;
import vn.uit.edu.msshop.inventory.application.port.out.LoadInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.SaveInventoryPort;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.ReservedQuantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Component
@RequiredArgsConstructor
public class SyncDBScheduler {
    private final SaveInventoryPort savePort;
    private final LoadInventoryPort loadPort;
    private final InventoryUpdatedDocumentRepository inventoryUpdatedRepo;
    @Scheduled(fixedRate=5000)
    
    public void syncDatabase() {
        System.out.println("Call sync jobbbbbbbb");
        int currentPage=0;
        int batchSize=50;
        Pageable pageable = PageRequest.of(currentPage, batchSize);
        Page<InventoryUpdatedDocument> batch = inventoryUpdatedRepo.findByIsReadOrderByCreatedAtAsc(false, pageable);
        int totalPages=batch.getTotalPages();
        currentPage = batch.getNumber();
        while(currentPage<=totalPages-1) {
            processOneBatch(batch.getContent());
            currentPage++;
            pageable = PageRequest.of(currentPage, batchSize);
            batch = inventoryUpdatedRepo.findByIsReadOrderByCreatedAtAsc(false, pageable);

        }

    }
    private Inventory findByVariantIdInList(List<Inventory> inventories, VariantId variantId) {
        for(Inventory i: inventories) {
            if(i.getVariantId().value().equals(variantId.value())) return i;
            
        }
        return null;

    }
    @Transactional
    private void processOneBatch(List<InventoryUpdatedDocument> inventoryUpdatedDocuments) {
        List<Inventory> inventories = loadPort.findByListVariantId(inventoryUpdatedDocuments.stream().map(item->new VariantId(item.getVariantId())).toList());
        System.out.println("Inventories size "+inventories.size());
        Set<Inventory> toSaves = new HashSet<>();
        for(InventoryUpdatedDocument eventDocument: inventoryUpdatedDocuments) {
            Inventory i = findByVariantIdInList(inventories, new VariantId(eventDocument.getVariantId()));
            if(i==null) throw new RuntimeException("Invalid code");
            System.out.println("Match , saving to db");
            System.out.println("New uantity "+eventDocument.getNewQuantity());
            
            final var updateInfo = Inventory.UpdateInfo.builder().inventoryId(i.getId()).quantity(new Quantity(eventDocument.getNewQuantity())).reservedQuantity(new ReservedQuantity(eventDocument.getNewReservedQuantity())).build();
            if(toSaves.contains(i)) {
                toSaves.remove(i);
            }
            toSaves.add(i.applyUpdateInfo(updateInfo));
            eventDocument.setRead(true);
            eventDocument.setUpdatedAt(Instant.now());

        }
        savePort.saveFromSet(toSaves);
        inventoryUpdatedRepo.saveAll(inventoryUpdatedDocuments);
    }
}
