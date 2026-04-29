package vn.edu.uit.msshop.product.category.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryImageView;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.query.FindCategoryImageUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

@Service
@RequiredArgsConstructor
public class FindCategoryImageService implements FindCategoryImageUseCase {
    private final LoadCategoryPort loadPort;
    private final CategoryViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public CategoryImageView findImageById(
            final CategoryId id) {
        return this.loadPort.loadById(id)
                .map(this.mapper::toImageView)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
