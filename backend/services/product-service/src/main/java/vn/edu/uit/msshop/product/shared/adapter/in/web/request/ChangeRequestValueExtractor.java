package vn.edu.uit.msshop.product.shared.adapter.in.web.request;

import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.ValueExtractor;

public class ChangeRequestValueExtractor
        implements ValueExtractor<ChangeRequest<@ExtractedValue ?>> {
    @Override
    public void extractValues(
            final ChangeRequest<?> originalValue,
            final ValueReceiver receiver) {
        if (originalValue != null) {
            receiver.value(null, originalValue.value());
        }
    }
}
