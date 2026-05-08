package vn.uit.edu.msshop.notification.dbcleaner;

import vn.uit.edu.msshop.notification.adapter.out.event.EventDocumentRepository;
import vn.uit.edu.msshop.notification.adapter.out.persistence.EmailRepository;

//@Component

public class DBcleaner {
    private EmailRepository emailRepo;
    private EventDocumentRepository eventDocumentRepo;
    public DBcleaner(EmailRepository emailRepo, EventDocumentRepository eventDocumentRepo) {
        this.emailRepo=emailRepo;
        this.eventDocumentRepo=eventDocumentRepo;
        eventDocumentRepo.deleteAll();
        emailRepo.deleteAll();
    }
}
