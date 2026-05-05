package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductOption;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

public record UpdateProductOptionCommand(ProductId id,int optionIndex,ProductOption newOption,ProductVersion expectedVersion){}
