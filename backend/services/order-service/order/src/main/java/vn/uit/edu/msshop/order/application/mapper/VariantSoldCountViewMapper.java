package vn.uit.edu.msshop.order.application.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.order.application.dto.query.VariantSoldCountView;
import vn.uit.edu.msshop.order.domain.model.VariantSoldCount;

@Component
public class VariantSoldCountViewMapper {
    public VariantSoldCountView toView(VariantSoldCount domain) {
        return new VariantSoldCountView(domain.getId().value(), domain.getSoldCount().value());
    }
}
