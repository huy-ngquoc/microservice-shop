package vn.uit.edu.msshop.order.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.order.adapter.in.web.response.VariantSoldCountResponse;
import vn.uit.edu.msshop.order.application.dto.query.VariantSoldCountView;

@Component
public class VariantSoldCountWebMapper {
    public VariantSoldCountResponse toResponse(VariantSoldCountView view) {
        return new VariantSoldCountResponse(view.getVariantId(), view.getSoldCount());
    }

}
