package vn.edu.uit.msshop.product.shared.adapter.in.web;

import java.time.Instant;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.shared.adapter.in.web.response.ApiErrorResponse;
import vn.edu.uit.msshop.product.shared.adapter.in.web.response.ValidationError;
import vn.edu.uit.msshop.product.shared.application.exception.BusinessRuleException;
import vn.edu.uit.msshop.product.shared.application.exception.NotFoundException;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            final NotFoundException ex,
            final HttpServletRequest request) {
        final var message = String.format(
                "Resource not found: %s",
                ex.getMessage());

        log.debug(message);

        final var status = HttpStatus.NOT_FOUND;
        final var response = GlobalExceptionHandler.buildResponse(
                status,
                message,
                request);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessRule(
            final BusinessRuleException ex,
            final HttpServletRequest request) {
        final var message = String.format(
                "Business rule violation: %s",
                ex.getMessage());

        log.debug(message);

        final var status = HttpStatus.UNPROCESSABLE_CONTENT;
        final var response = GlobalExceptionHandler.buildResponse(
                status,
                message,
                request);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ApiErrorResponse> handleOptimisticLock(
            final OptimisticLockException ex,
            final HttpServletRequest request) {
        final var message = String.format(
                "Optimistic lock conflict: %s",
                ex.getMessage());

        log.debug(message);

        final var status = HttpStatus.CONFLICT;
        final var response = GlobalExceptionHandler.buildResponse(
                status,
                message,
                request);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(
            final MethodArgumentTypeMismatchException ex,
            final HttpServletRequest request) {
        final var requiredType = ex.getRequiredType();
        final String typeName;
        if (requiredType != null) {
            typeName = requiredType.getSimpleName();
        } else {
            typeName = "unknown";
        }

        final var message = String.format(
                "The parameter '%s' should be of type %s",
                ex.getName(),
                typeName);

        log.debug(message);

        final var status = HttpStatus.BAD_REQUEST;
        final var response = GlobalExceptionHandler.buildResponse(
                status,
                message,
                request);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            final MethodArgumentNotValidException ex,
            final HttpServletRequest request) {
        final var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ValidationError(
                        fe.getField(),
                        Objects.requireNonNullElse(
                                fe.getDefaultMessage(),
                                "Validation error")))
                .toList();

        final var message = String.format(
                "Validation failed: %s",
                fieldErrors);

        log.debug(message);

        final var status = HttpStatus.BAD_REQUEST;
        final var response = new ApiErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                Instant.now(),
                request.getRequestURI(),
                fieldErrors);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            final ConstraintViolationException ex,
            final HttpServletRequest request) {
        final var errors = ex.getConstraintViolations().stream()
                .map(cv -> new ValidationError(
                        cv.getPropertyPath().toString(),
                        cv.getMessage()))
                .toList();

        final var message = String.format(
                "Constraint violation: %s",
                errors);

        log.debug(message);

        final var status = HttpStatus.BAD_REQUEST;
        final var response = new ApiErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                Instant.now(),
                request.getRequestURI(),
                errors);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleNotReadable(
            final HttpMessageNotReadableException ex,
            final HttpServletRequest request) {
        final var message = String.format(
                "Malformed request body: %s",
                ex.getMessage());

        log.debug(message);

        final var status = HttpStatus.BAD_REQUEST;
        final var response = GlobalExceptionHandler.buildResponse(
                status,
                message,
                request);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParam(
            final MissingServletRequestParameterException ex,
            final HttpServletRequest request) {
        final var message = String.format(
                "Missing request parameter: %s",
                ex.getMessage());

        log.debug(message);

        final var status = HttpStatus.BAD_REQUEST;
        final var response = GlobalExceptionHandler.buildResponse(
                status,
                message,
                request);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> handleDomainException(
            final DomainException ex,
            final HttpServletRequest request) {
        log.warn("Domain invariant violation", ex);

        final var status = HttpStatus.INTERNAL_SERVER_ERROR;
        final var response = GlobalExceptionHandler.buildResponse(
                status,
                "Internal server error",
                request);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFoundException(
            final NoResourceFoundException ex,
            final HttpServletRequest request) {
        final var message = String.format(
                "Resource not found: %s",
                ex.getMessage());

        log.debug(message);

        final var status = HttpStatus.NOT_FOUND;
        final var response = GlobalExceptionHandler.buildResponse(
                status,
                message,
                request);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(
            final Exception ex,
            final HttpServletRequest request) {
        log.error("Unexpected error", ex);

        final var status = HttpStatus.INTERNAL_SERVER_ERROR;
        final var response = GlobalExceptionHandler.buildResponse(
                status,
                "Internal server error",
                request);

        return ResponseEntity.status(status).body(response);
    }

    private static ApiErrorResponse buildResponse(
            final HttpStatus status,
            final String message,
            final HttpServletRequest request) {
        return new ApiErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                Instant.now(),
                request.getRequestURI(),
                null);
    }
}
