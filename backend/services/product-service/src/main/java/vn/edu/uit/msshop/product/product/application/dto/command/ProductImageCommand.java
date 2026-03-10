package vn.edu.uit.msshop.product.product.application.dto.command;

public record ProductImageCommand(
        byte[] bytes,
        String originalFilename,
        String contentType) {
}
