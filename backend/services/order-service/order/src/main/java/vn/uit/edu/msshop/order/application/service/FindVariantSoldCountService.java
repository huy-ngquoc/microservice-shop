package vn.uit.edu.msshop.order.application.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.application.dto.query.VariantSoldCountView;
import vn.uit.edu.msshop.order.application.exception.VariantSoldCountNotFoundException;
import vn.uit.edu.msshop.order.application.mapper.VariantSoldCountViewMapper;
import vn.uit.edu.msshop.order.application.port.in.FindVariantSoldCountUseCase;
import vn.uit.edu.msshop.order.application.port.out.FindVariantSoldCountPort;
import vn.uit.edu.msshop.order.bootstrap.config.cache.CacheNames;
import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class FindVariantSoldCountService implements FindVariantSoldCountUseCase {
    private final VariantSoldCountViewMapper mapper;
    private final FindVariantSoldCountPort findPort;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.VARIANT_SOLD_COUNT,
            key = "#id.value()")
    public VariantSoldCountView findById(
            VariantId id) {
        final var result = findPort.findById(id).orElseThrow(() -> new VariantSoldCountNotFoundException(id));
        return mapper.toView(result);
    }

    @Override
    @Transactional(
            readOnly = true)
    public List<VariantSoldCountView> findByListId(
            List<VariantId> ids) {
        final var result = findPort.findByIdInList(ids);
        return result.stream().map(mapper::toView).toList();
    }

    @Override
    @Transactional(
            readOnly = true)
    public List<VariantSoldCountView> findAll() {
        final var result = findPort.findAll();
        return result.stream().map(mapper::toView).toList();
    }

}
