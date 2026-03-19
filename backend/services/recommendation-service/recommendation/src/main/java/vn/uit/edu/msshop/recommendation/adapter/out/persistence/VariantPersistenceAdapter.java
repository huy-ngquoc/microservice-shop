package vn.uit.edu.msshop.recommendation.adapter.out.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.recommendation.adapter.out.persistence.mapper.VariantDocumentMapper;
import vn.uit.edu.msshop.recommendation.application.exception.VariantNotFoundException;
import vn.uit.edu.msshop.recommendation.application.port.out.DeleteVariantPort;
import vn.uit.edu.msshop.recommendation.application.port.out.LoadVariantPort;
import vn.uit.edu.msshop.recommendation.application.port.out.SaveVariantPort;
import vn.uit.edu.msshop.recommendation.domain.model.Variant;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantId;

@Component
@RequiredArgsConstructor
public class VariantPersistenceAdapter implements LoadVariantPort, SaveVariantPort, DeleteVariantPort {
    private final VariantDocumentMapper mapper;
    private final VariantRepository repo;
    private final MongoTemplate template;
    @Override
    public Variant loadById(VariantId id) {
        VariantMongoDbDocument document = repo.findById(id.value()).orElseThrow(()-> new VariantNotFoundException(id));
        return mapper.toDomain(document);
    }

    @Override
    public List<Variant> loadByTarget(String age, String gender, String shape, String bodyShape) {
        List<String> searchTags = new ArrayList<>();
        if (age != null) searchTags.add(age);
        if (gender != null) searchTags.add(gender);
        if (shape != null) searchTags.add(shape);
        if (bodyShape != null) searchTags.add(bodyShape);
        Query query = new Query();
        query.addCriteria(Criteria.where("targets").all(searchTags));
        List<VariantMongoDbDocument> result =template.find(query, VariantMongoDbDocument.class);
        return result.stream().map(mapper::toDomain).toList();
    }

    @Override
    public Variant save(Variant variant) {
        final var toSave = mapper.toDocument(variant);
        final var result = repo.save(toSave);
        return mapper.toDomain(result);
    }

    @Override
    public void deleteById(VariantId id) {
        repo.deleteById(id.value());
    }

}
