package vn.edu.uit.msshop.product.brand.application.port.in.query;

import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public final class BrandLookupUseCases {
    public interface ListActive {
        PageResponseDto<BrandView> listActive(
                final PageRequestDto pageRequest);
    }

    public interface ListSoftDeleted {
        PageResponseDto<BrandView> listSoftDeleted(
                final PageRequestDto pageRequest);
    }

    public interface CheckExistsById {
        boolean existsById(
                final BrandId id);
    }

    public interface FindActiveById {
        BrandView findActiveById(
                final BrandId id);
    }

    public interface FindActiveLogoById {
        BrandLogoView findActiveLogoById(
                final BrandId id);
    }

    public interface FindSoftDeletedById {
        BrandView findSoftDeletedById(
                final BrandId id);
    }
}
