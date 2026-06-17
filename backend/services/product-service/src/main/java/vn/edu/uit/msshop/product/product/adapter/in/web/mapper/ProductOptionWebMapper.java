package vn.edu.uit.msshop.product.product.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductOptionAdditionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductOptionRemovalRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductOptionUpdateRequest;
import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionAdditionByIdCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionRemovalByIdCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionUpdateByIdCommand;

@Component
public class ProductOptionWebMapper {
    public ProductOptionAdditionByIdCommand toAdditionByIdCommand(
            final UUID productId,
            final ProductOptionAdditionRequest request) {
        return new ProductOptionAdditionByIdCommand(
                productId,
                request.option(),
                request.defaultTrait(),
                request.version());
    }

    public ProductOptionUpdateByIdCommand toUpdateByIdCommand(
            final UUID productId,
            final int optionIndex,
            final ProductOptionUpdateRequest request) {
        return new ProductOptionUpdateByIdCommand(
                productId,
                optionIndex,
                request.option(),
                request.version());
    }

    public ProductOptionRemovalByIdCommand toRemovalByIdCommand(
            final UUID productId,
            final int optionIndex,
            final ProductOptionRemovalRequest request) {
        return new ProductOptionRemovalByIdCommand(
                productId,
                optionIndex,
                request.version());
    }
}
