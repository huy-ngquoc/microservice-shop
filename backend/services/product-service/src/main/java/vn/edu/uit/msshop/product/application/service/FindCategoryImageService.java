package vn.edu.uit.msshop.product.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.application.dto.query.CategoryImageView;
import vn.edu.uit.msshop.product.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.application.port.in.FindCategoryImageUseCase;
import vn.edu.uit.msshop.product.application.port.out.LoadCategoryPort;
import vn.edu.uit.msshop.product.domain.model.category.Category;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;

@Service
@RequiredArgsConstructor
public class FindCategoryImageService implements FindCategoryImageUseCase {
    private final LoadCategoryPort loadPort;
    private final CategoryViewMapper mapper;

    public CategoryImageView findById(
            final CategoryId id) {
        return this.loadPort.loadById(id)
                .map(Category::getImage)
                .map(this.mapper::toView)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
