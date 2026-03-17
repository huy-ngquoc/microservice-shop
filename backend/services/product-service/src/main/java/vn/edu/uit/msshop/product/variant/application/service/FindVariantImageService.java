package vn.edu.uit.msshop.product.variant.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantImageView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.FindVariantImageUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;

@Service
@RequiredArgsConstructor
public class FindVariantImageService implements FindVariantImageUseCase {
    private final LoadVariantPort loadPort;
    private final VariantViewMapper mapper;

    @Override
    public VariantImageView findImageById(
            final VariantId id) {
        return this.loadPort.loadById(id)
                .map(this.mapper::toImageView)
                .orElseThrow(() -> new VariantNotFoundException(id));
    }
}
