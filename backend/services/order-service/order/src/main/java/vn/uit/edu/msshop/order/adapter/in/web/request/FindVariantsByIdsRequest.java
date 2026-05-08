package vn.uit.edu.msshop.order.adapter.in.web.request;

import java.util.Set;
import java.util.UUID;



public record FindVariantsByIdsRequest(
        
        Set<UUID> ids) {
}
