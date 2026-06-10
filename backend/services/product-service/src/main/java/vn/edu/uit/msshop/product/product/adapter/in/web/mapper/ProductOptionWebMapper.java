package vn.edu.uit.msshop.product.product.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductOptionAdditionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductOptionRemovalRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductOptionUpdateRequest;
import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionAdditionCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionRemovalCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionUpdateCommand;

@Component
public class ProductOptionWebMapper {
    public ProductOptionAdditionCommand toAdditionCommand(
            final UUID productId,
            final ProductOptionAdditionRequest request) {
        return new ProductOptionAdditionCommand(
                productId,
                request.option(),
                request.defaultTrait(),
                request.version());
    }

    public ProductOptionUpdateCommand toUpdateCommand(
            final UUID productId,
            final int optionIndex,
            final ProductOptionUpdateRequest request) {
        return new ProductOptionUpdateCommand(
                productId,
                optionIndex,
                request.option(),
                request.version());
    }

    public ProductOptionRemovalCommand toRemovalCommand(
            final UUID productId,
            final int optionIndex,
            final ProductOptionRemovalRequest request) {
        return new ProductOptionRemovalCommand(
                productId,
                optionIndex,
                request.defaultPrice(),
                request.version());
    }
}
