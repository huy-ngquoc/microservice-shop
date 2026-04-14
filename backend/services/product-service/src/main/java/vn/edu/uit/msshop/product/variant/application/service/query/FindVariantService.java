package vn.edu.uit.msshop.product.variant.application.service.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.query.FindVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class FindVariantService implements FindVariantUseCase {
    private final LoadVariantPort loadPort;
    private final VariantViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public VariantView findById(
            final VariantId id) {
        return this.loadPort.loadById(id)
                .map(this.mapper::toView)
                .orElseThrow(() -> new VariantNotFoundException(id));
    }

    @Override
    public List<VariantView> findByListIds(
                List<VariantId> ids) {
        return this.loadPort.loadByListIds(ids).stream().map(mapper::toView).toList();
    }
}
