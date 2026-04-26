package vn.uit.edu.msshop.notification.config;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.notification.adapter.out.persistence.EmailRepository;

@Component

public class DBCleaner {
    private EmailRepository emailRepo;
    public DBCleaner(EmailRepository emailRepo) {
        this.emailRepo=emailRepo;
        emailRepo.deleteAll();
    }
}
