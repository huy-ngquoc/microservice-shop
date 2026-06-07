package vn.edu.uit.msshop.shared.adapter.out.cloudinary;

public final class CloudinaryPublicIds {

    private CloudinaryPublicIds() {
    }

    public static String extractKeyFromTemp(
            final String publicId) {
        final var prefix = CloudinaryFolders.TEMP + "/";
        if (!publicId.startsWith(prefix)) {
            throw new IllegalArgumentException("Image key must be in temp folder");
        }

        return publicId.substring(prefix.length());
    }
}
