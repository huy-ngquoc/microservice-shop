package vn.edu.uit.msshop.product.shared.adapter.exception;

public final class ImageRenameFailedException
        extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Rename image failed";

    public ImageRenameFailedException() {
        super(DEFAULT_MESSAGE);
    }

    public ImageRenameFailedException(
            final Exception exception) {
        super(DEFAULT_MESSAGE, exception);
    }

    public ImageRenameFailedException(
            final String msg,
            final Exception exception) {
        super(msg, exception);
    }
}
