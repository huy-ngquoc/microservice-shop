package vn.edu.uit.msshop.shared.adapter.out.cloudinary;

public final class CloudinaryPublicIds {

    public static final String TEMP_PREFIX = CloudinaryFolders.TEMP + "/";
    public static final int TEMP_PREFIX_LENGTH = 5;

    private CloudinaryPublicIds() {
    }

    public static String extractKeyFromTemp(
            final String publicId) {
        if (!publicId.startsWith(TEMP_PREFIX)) {
            throw new IllegalArgumentException("Image key must be in temp folder");
        }

        return publicId.substring(TEMP_PREFIX.length());
    }
}
