package vn.uit.edu.msshop.rating.adapter.exception;
public class ImageUploadFailException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Upload avatar failed";

    public ImageUploadFailException() {
        super(DEFAULT_MESSAGE);
    }

    public ImageUploadFailException(
            final Exception exception) {
        super(DEFAULT_MESSAGE, exception);
    }
}
