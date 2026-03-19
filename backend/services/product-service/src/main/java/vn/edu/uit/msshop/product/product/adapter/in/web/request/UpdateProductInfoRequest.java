package vn.edu.uit.msshop.product.product.adapter.in.web.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.product.domain.model.ProductName;
import vn.edu.uit.msshop.product.shared.adapter.in.web.request.ChangeRequest;

public record UpdateProductInfoRequest(
        ChangeRequest<@NotBlank @Size(
                max = ProductName.MAX_LENGTH) String> name,

        ChangeRequest<UUID> categoryId,

        ChangeRequest<UUID> brandId,

        long expectedVersion) {
}
