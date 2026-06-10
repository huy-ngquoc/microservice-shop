package vn.edu.uit.msshop.product.product.adapter.in.web.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.shared.adapter.in.web.request.ChangeRequest;

public record ProductInfoUpdateRequest(
        ChangeRequest<
                @NotBlank @Size(
                        max = ProductName.MAX_LENGTH) String> name,

        ChangeRequest<UUID> categoryId,

        ChangeRequest<UUID> brandId,

        @NotNull
        Long version) {
}
