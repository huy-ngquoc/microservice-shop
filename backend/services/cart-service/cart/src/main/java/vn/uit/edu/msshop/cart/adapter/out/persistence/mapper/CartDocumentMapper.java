
package vn.uit.edu.msshop.cart.adapter.out.persistence.mapper;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.cart.adapter.out.persistence.CartDocument;
import vn.uit.edu.msshop.cart.adapter.out.persistence.CartItemDocument;
import vn.uit.edu.msshop.cart.domain.model.Cart;
import vn.uit.edu.msshop.cart.domain.model.CartDetail;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Amount;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ImageKey;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ProductName;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UnitPrice;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantTraits;

@Component
public class CartDocumentMapper {
    public CartDetail toDomain(
            CartItemDocument document) {
        return new CartDetail(new VariantId(UUID.fromString(document.getVariantId())),
                new ProductName(document.getName()), new UnitPrice(document.getPrice().longValue()),
                new ImageKey(document.getImageKey()), new VariantTraits(document.getTraits()),
                new Amount(document.getAmount()));
    }

    public Cart toDomain(
            CartDocument document) {
        return new Cart(new UserId(document.getUserId()), document.getItems().stream().map(this::toDomain).toList());
    }

    public CartItemDocument toDocument(
            CartDetail domain) {
        return new CartItemDocument(domain.getVariantId().value().toString(), domain.getTraits().value(),
                domain.getImageKey().value(), domain.getName().value(), new BigDecimal(domain.getPrice().value()),
                domain.getAmount().value());
    }

    public CartDocument toDocument(
            Cart domain) {
        return new CartDocument(domain.getUserId().value(),
                domain.getDetails().stream().map(this::toDocument).toList());
    }
}
