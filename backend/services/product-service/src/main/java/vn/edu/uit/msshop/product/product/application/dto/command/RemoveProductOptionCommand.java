package vn.edu.uit.msshop.product.product.application.dto.command;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

public record RemoveProductOptionCommand(ProductId id,int optionIndex,@Nullable ProductPrice defaultPrice,ProductVersion expectedVersion){}
