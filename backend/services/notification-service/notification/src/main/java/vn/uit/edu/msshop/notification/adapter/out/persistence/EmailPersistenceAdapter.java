package vn.uit.edu.msshop.notification.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.notification.adapter.out.persistence.mapper.EmailDocumentMapper;
import vn.uit.edu.msshop.notification.application.port.out.LoadEmailPort;
import vn.uit.edu.msshop.notification.application.port.out.SaveEmailPort;
import vn.uit.edu.msshop.notification.domain.model.Email;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailId;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailStatus;

@Component
@RequiredArgsConstructor
public class EmailPersistenceAdapter implements LoadEmailPort, SaveEmailPort{
    private final EmailDocumentMapper mapper;
    private final EmailRepository repository;

    @Override
    public Optional<Email> loadByEmailId(EmailId emailId) {
        Optional<EmailDocument> result = repository.findById(emailId.value());
        if(result.isEmpty()) return Optional.empty();
        return Optional.of(mapper.toDomain(result.get()));
    }

    @Override
    public List<Email> loadByEmailStatus(EmailStatus emailStatus) {
        return repository.findByEmailStatus(emailStatus.value()).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Email save(Email email) {
        return mapper.toDomain(repository.save(mapper.toDocument(email)));
    }

    @Override
    public List<Email> saveAll(List<Email> emails) {
        List<EmailDocument> documents = emails.stream().map(mapper::toDocument).toList();
        return repository.saveAll(documents).stream().map(mapper::toDomain).toList();
    }

}
