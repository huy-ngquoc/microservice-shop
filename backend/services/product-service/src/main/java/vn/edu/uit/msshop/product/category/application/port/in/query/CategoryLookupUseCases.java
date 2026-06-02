package vn.edu.uit.msshop.product.category.application.port.in.query;

import vn.edu.uit.msshop.product.category.application.dto.view.CategoryImageView;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public final class CategoryLookupUseCases {

    private CategoryLookupUseCases() {
    }

    public interface ListActive {
        PageResponseDto<CategoryView> listActive(
                final PageRequestDto pageRequest);
    }

    public interface ListSoftDeleted {
        PageResponseDto<CategoryView> listSoftDeleted(
                final PageRequestDto pageRequest);
    }

    public interface CheckExistsById {
        boolean existsById(
                final CategoryId id);
    }

    public interface FindActiveById {
        CategoryView findActiveById(
                final CategoryId id);
    }

    public interface FindActiveImageById {
        CategoryImageView findActiveImageById(
                final CategoryId id);
    }

    public interface FindSoftDeletedById {
        CategoryView findSoftDeletedById(
                final CategoryId id);
    }
}
