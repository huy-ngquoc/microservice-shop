package vn.edu.uit.msshop.product.variant.application.dto.command;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public record PostImageCommand(VariantId variantId, MultipartFile image, long version) {

}
