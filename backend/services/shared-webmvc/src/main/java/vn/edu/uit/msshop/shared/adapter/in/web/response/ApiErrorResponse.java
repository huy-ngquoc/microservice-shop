package vn.edu.uit.msshop.shared.adapter.in.web.response;

import java.time.Instant;
import java.util.List;

import org.jspecify.annotations.Nullable;

public record ApiErrorResponse(
        int status,
        String error,
        String message,
        Instant timestamp,
        String path,
        @Nullable
        List<ValidationError> validationErrors) {
}
