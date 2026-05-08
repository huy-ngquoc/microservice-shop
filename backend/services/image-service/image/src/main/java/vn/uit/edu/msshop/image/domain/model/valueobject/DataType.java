package vn.uit.edu.msshop.image.domain.model.valueobject;

import java.util.Set;

public record DataType(String value) {
    private static final Set<String> VALID_DATATYPE = Set.of("Profile","Brand","Category","Product","Product Variant","Rating");
    public DataType {
        if(!VALID_DATATYPE.contains(value)) {
            throw new IllegalArgumentException("Invalid data type");
        }
    }

}
