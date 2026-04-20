package vn.uit.edu.msshop.inventory.dbcleaner;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.ForceCancellOrderDocumentRepository;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.InventoryUpdatedDocumentRepository;

@Component
public class EventDBCleanerNew {
    private InventoryUpdatedDocumentRepository inventoryUpdatedRepo;
    private ForceCancellOrderDocumentRepository forceCancellOrderRepo;
    private EventDocumentRepository eventDocumentRepo;

    public EventDBCleanerNew(EventDocumentRepository eventDocumentRepo, ForceCancellOrderDocumentRepository forceCancellOrderRepo, InventoryUpdatedDocumentRepository inventoryUpdatedRepo) {
        System.out.println("Clearedddd event");
        this.eventDocumentRepo = eventDocumentRepo;
        this.forceCancellOrderRepo = forceCancellOrderRepo;
        this.inventoryUpdatedRepo = inventoryUpdatedRepo;
        eventDocumentRepo.deleteAll();
        forceCancellOrderRepo.deleteAll();
        inventoryUpdatedRepo.deleteAll();
    }

}
