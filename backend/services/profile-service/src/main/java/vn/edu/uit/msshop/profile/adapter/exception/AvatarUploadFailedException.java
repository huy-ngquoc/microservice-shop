package vn.edu.uit.msshop.profile.adapter.exception;

public class AvatarUploadFailedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Upload avatar failed";

    public AvatarUploadFailedException() {
        super(DEFAULT_MESSAGE);
    }

    public AvatarUploadFailedException(
            final Exception exception) {
        super(DEFAULT_MESSAGE, exception);
    }
}
