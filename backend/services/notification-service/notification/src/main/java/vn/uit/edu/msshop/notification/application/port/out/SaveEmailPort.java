package vn.uit.edu.msshop.notification.application.port.out;

import java.util.List;

import vn.uit.edu.msshop.notification.domain.model.Email;

public interface SaveEmailPort {
    public Email save(Email email);
    public List<Email> saveAll(List<Email> emails);
}
