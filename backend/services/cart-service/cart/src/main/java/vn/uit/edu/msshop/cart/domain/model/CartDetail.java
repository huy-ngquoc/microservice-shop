package vn.uit.edu.msshop.cart.domain.model;



import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Amount;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Color;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ImageUrls;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ProductName;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Size;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UnitPrice;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@Builder(
        access = AccessLevel.PRIVATE)
public class CartDetail {
    private VariantId variantId;
    private ImageUrls imageUrls;
    private ProductName name;
    private UnitPrice price;
    private Color color;
    private Size size;
    private Amount amount;

    @Builder
    public static record Draft(VariantId variantId, ImageUrls imageUrls, ProductName name, UnitPrice price, Color color, Size size, Amount amount) {

    }
    @Builder
    public static record Snapshot(VariantId variantId, ImageUrls imageUrls, ProductName name, UnitPrice price, Color color, Size size, Amount amount) {

    }
    @Builder
    public static record UpdateAmount(VariantId variantId, Amount amount) {

    }
    @Builder
    public static record UpdateInfo(VariantId variantId, ImageUrls imageUrls, ProductName name, UnitPrice price, Color color, Size size) {

    }

    public static CartDetail create(Draft draft) {
        if(draft==null) throw new IllegalArgumentException("Invalid draft");
        return CartDetail.builder().variantId(draft.variantId()).imageUrls(draft.imageUrls()).name(draft.name()).price(draft.price()).color(draft.color()).size(draft.size()).amount(draft.amount()).build();
    }
    public Snapshot snapshot() {
        return CartDetail.Snapshot.builder().variantId(this.variantId).imageUrls(this.imageUrls).name(this.name).price(this.price).color(this.color).size(this.size).amount(this.amount).build();
    }
    public CartDetail applyUpdateAmount(CartDetail.UpdateAmount u) {
        if(u.amount.value()==this.amount.value()) return this;
        return CartDetail.builder().variantId(this.variantId).imageUrls(this.imageUrls).name(this.name).price(this.price).color(this.color).size(this.size).amount(u.amount()).build();
    
    }
    public CartDetail applyUpdateInfo(CartDetail.UpdateInfo u) {
        if(!u.variantId.value().equals(this.variantId.value())) throw new IllegalArgumentException("Invalid argument");
        return CartDetail.builder().variantId(this.variantId).imageUrls(u.imageUrls).name(u.name).price(u.price).color(u.color).size(u.size).amount(this.amount).build();
    }
    


}
