package vn.uit.edu.msshop.rating.adapter.out.cloudinary;
public enum CloudDinaryResourceType {
    IMAGE("image"),
    VIDEO("video"),
    RAW("raw"),
    AUTO("auto");

    private final String value;

    CloudDinaryResourceType(
            String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
