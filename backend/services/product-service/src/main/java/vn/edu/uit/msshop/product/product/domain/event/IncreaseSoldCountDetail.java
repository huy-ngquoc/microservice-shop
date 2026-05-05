package vn.edu.uit.msshop.product.product.domain.event;

import java.util.UUID;

public record IncreaseSoldCountDetail(UUID variantId,int amount){

}
