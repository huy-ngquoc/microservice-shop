package vn.edu.uit.msshop.product.shared.domain.product;

import vn.edu.uit.msshop.shared.domain.Domains;

public final class ProductNameConstraints {

    public static final int MAX_LENGTH = 200;
    public static final int MAX_RAW_LENGTH = (int) (ProductNameConstraints.MAX_LENGTH
            * Domains.RAW_LENGTH_TOLERANCE_FACTOR);

    private ProductNameConstraints() {
    }
}
