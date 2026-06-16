package vn.edu.uit.msshop.product.shared.domain.variant;

import vn.edu.uit.msshop.shared.domain.Domains;

public final class VariantTargetConstraints {

    public static final int MAX_LENGTH = 30;
    public static final int MAX_RAW_LENGTH = (int) (VariantTargetConstraints.MAX_LENGTH
            * Domains.RAW_LENGTH_TOLERANCE_FACTOR);

    private VariantTargetConstraints() {
    }
}
