package vn.edu.uit.msshop.shared.adapter.in.web.response;

public record ValidationError(
        String field,
        String message) {
}
