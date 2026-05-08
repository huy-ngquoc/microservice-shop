package vn.uit.edu.msshop.notification.application.port.out;

import java.util.List;
import java.util.Optional;

import vn.uit.edu.msshop.notification.domain.model.Email;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailId;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailStatus;

public interface LoadEmailPort {
    public Optional<Email> loadByEmailId(EmailId emailId);
    public List<Email> loadByEmailStatus(EmailStatus emailStatus);
}
