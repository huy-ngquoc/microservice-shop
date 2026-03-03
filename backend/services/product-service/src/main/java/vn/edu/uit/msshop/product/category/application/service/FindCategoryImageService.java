package vn.edu.uit.msshop.product.category.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.dto.query.CategoryImageView;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.FindCategoryImageUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;

@Service
@RequiredArgsConstructor
public class FindCategoryImageService implements FindCategoryImageUseCase {
    private final LoadCategoryPort loadPort;
    private final CategoryViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public CategoryImageView findById(
            final CategoryId id) {
        return this.loadPort.loadById(id)
                .map(Category::getImage)
                .map(this.mapper::toView)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
