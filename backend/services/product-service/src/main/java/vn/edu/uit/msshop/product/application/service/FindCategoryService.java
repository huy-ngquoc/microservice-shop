package vn.edu.uit.msshop.product.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.application.dto.query.CategoryView;
import vn.edu.uit.msshop.product.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.application.port.in.FindCategoryUseCase;
import vn.edu.uit.msshop.product.application.port.out.LoadCategoryPort;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;

@Service
@RequiredArgsConstructor
public class FindCategoryService implements FindCategoryUseCase {
    private final LoadCategoryPort loadPort;
    private final CategoryViewMapper mapper;

    public CategoryView findById(
            final CategoryId id) {
        return this.loadPort.loadById(id)
                .map(this.mapper::toView)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
