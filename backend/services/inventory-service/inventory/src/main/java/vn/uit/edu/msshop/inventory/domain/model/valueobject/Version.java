package vn.uit.edu.msshop.inventory.domain.model.valueobject;
public record Version(long value) {
    public Version {
        if(value<0) throw new IllegalArgumentException("Invalid version");
    }
}
