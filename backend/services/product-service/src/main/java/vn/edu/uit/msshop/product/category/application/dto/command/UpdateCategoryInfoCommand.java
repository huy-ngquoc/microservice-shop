package vn.edu.uit.msshop.product.category.application.dto.command;

import org.jspecify.annotations.NullMarked;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryName;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryVersion;
import vn.edu.uit.msshop.shared.application.dto.Change;

@NullMarked public record UpdateCategoryInfoCommand(CategoryId id,Change<CategoryName>name,CategoryVersion expectedVersion){}
