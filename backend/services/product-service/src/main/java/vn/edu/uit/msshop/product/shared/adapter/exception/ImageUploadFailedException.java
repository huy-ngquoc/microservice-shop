package vn.edu.uit.msshop.product.shared.adapter.exception;

public final class ImageUploadFailedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Upload image failed";

    public ImageUploadFailedException() {
        super(DEFAULT_MESSAGE);
    }

    public ImageUploadFailedException(
            final Exception exception) {
        super(DEFAULT_MESSAGE, exception);
    }
}
