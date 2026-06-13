package vn.edu.uit.msshop.product.variant.application.dto.query;

import java.util.List;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;

public record ListVariantsQuery(
        PageRequestDto pageRequest,
        List<String> targetList) {

    public ListVariantsQuery {
        targetList = List.copyOf(targetList);
    }
}
