package vn.edu.uit.msshop.product.shared.application.exception;

public class NotFoundException extends RuntimeException {
    private final String resource;
    private final String identifier;

    public NotFoundException(
            final String resource,
            final String identifier) {
        this(resource, identifier, null);
    }

    public NotFoundException(
            final String resource,
            final String identifier,
            final Throwable cause) {
        super(NotFoundException.buildMessage(resource, identifier), cause);

        this.resource = resource;
        this.identifier = identifier;
    }

    public String getResource() {
        return this.resource;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    private static String buildMessage(
            final String resource,
            final String identifier) {
        final String r;
        if ((resource != null) && (!resource.isBlank())) {
            r = resource;
        } else {
            r = "Resource";
        }

        final String id;
        if ((identifier != null) && (!identifier.isBlank())) {
            id = identifier;
        } else {
            id = "unknown";
        }

        return r + " not found with ID: " + id;
    }
}
