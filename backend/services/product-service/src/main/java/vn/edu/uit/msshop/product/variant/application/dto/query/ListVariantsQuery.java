package vn.edu.uit.msshop.product.variant.application.dto.query;

import java.util.List;
import java.util.Objects;

import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTarget;

public record ListVariantsQuery(PageRequestDto pageRequest,List<VariantTarget>targets){public ListVariantsQuery{Objects.requireNonNull(pageRequest);

if(targets==null){targets=List.of();}else{targets=List.copyOf(targets);}}}
