package vn.edu.uit.msshop.profile.adapter.out.cloudinary;

public enum CloudinaryResourceType {
    IMAGE("image"),
    VIDEO("video"),
    RAW("raw"),
    AUTO("auto");

    private final String value;

    CloudinaryResourceType(
            String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
