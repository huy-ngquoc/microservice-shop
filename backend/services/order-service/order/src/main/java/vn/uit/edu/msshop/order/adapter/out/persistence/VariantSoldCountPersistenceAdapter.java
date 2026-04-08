package vn.uit.edu.msshop.order.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.out.persistence.mapper.VariantSoldCountDataMapper;
import vn.uit.edu.msshop.order.application.port.out.CreateVariantSoldCountPort;
import vn.uit.edu.msshop.order.application.port.out.DeleteVariantSoldCountPort;
import vn.uit.edu.msshop.order.application.port.out.FindVariantSoldCountPort;
import vn.uit.edu.msshop.order.application.port.out.UpdateVariantSoldCountPort;
import vn.uit.edu.msshop.order.domain.model.VariantSoldCount;
import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class VariantSoldCountPersistenceAdapter implements CreateVariantSoldCountPort, FindVariantSoldCountPort, UpdateVariantSoldCountPort, DeleteVariantSoldCountPort {
    private final VariantSoldCountRepository variantSoldCountRepo;
    private final VariantSoldCountDataMapper mapper;

    @Override
    public VariantSoldCount create(VariantSoldCount variantSoldCount) {
        final var entity = mapper.toDocument(variantSoldCount);
        return mapper.toDomain(variantSoldCountRepo.save(entity));
    }

    @Override
    public Optional<VariantSoldCount> findById(VariantId id) {
        final var result = variantSoldCountRepo.findById(id.value());
        if(result.isEmpty()) return Optional.empty();
        return Optional.of(mapper.toDomain(result.get()));
    }

    @Override
    public List<VariantSoldCount> findByIdInList(List<VariantId> ids) {
        final var result = variantSoldCountRepo.findByIdIn(ids.stream().map(VariantId::value).toList());
        return result.stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<VariantSoldCount> findAll() {
        final var result = variantSoldCountRepo.findAll();
        return result.stream().map(mapper::toDomain).toList();
    }

    @Override
    public VariantSoldCount update(VariantSoldCount variantSoldCount) {
        final var entity = mapper.toDocument(variantSoldCount);
        return mapper.toDomain(variantSoldCountRepo.save(entity));
    }

    @Override
    public void deleteById(VariantId id) {
        variantSoldCountRepo.deleteById(id.value());
    }

    @Override
    public List<VariantSoldCount> updateMany(List<VariantSoldCount> variantSoldCounts) {
        variantSoldCountRepo.saveAll(variantSoldCounts.stream().map(mapper::toDocument).toList());
        return variantSoldCounts;
    }
}
