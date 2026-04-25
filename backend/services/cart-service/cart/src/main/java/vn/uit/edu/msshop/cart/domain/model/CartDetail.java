package vn.uit.edu.msshop.cart.domain.model;



import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Amount;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ImageKey;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ProductName;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UnitPrice;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantTraits;
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@Builder(
        access = AccessLevel.PRIVATE)
public class CartDetail {
    private VariantId variantId;
    
    private ProductName name;
    private UnitPrice price;
    private ImageKey imageKey;
    private VariantTraits traits;
    private Amount amount;

    @Builder
    public static record Draft(VariantId variantId,ImageKey imageKey, ProductName name, UnitPrice price, VariantTraits traits, Amount amount) {

    }
    @Builder
    public static record Snapshot(VariantId variantId, ImageKey imageKey, ProductName name, UnitPrice price, VariantTraits traits, Amount amount) {

    }
    @Builder
    public static record UpdateAmount(VariantId variantId, Amount amount) {

    }
    @Builder
    public static record UpdateInfo(VariantId variantId, ImageKey imageKey, ProductName name, UnitPrice price, VariantTraits traits) {

    }

    public static CartDetail create(Draft draft) {
        if(draft==null) throw new IllegalArgumentException("Invalid draft");
        return CartDetail.builder().variantId(draft.variantId()).imageKey(draft.imageKey()).name(draft.name()).price(draft.price()).traits(draft.traits()).amount(draft.amount()).build();
    }
    public Snapshot snapshot() {
        return CartDetail.Snapshot.builder().variantId(this.variantId).imageKey(this.imageKey).name(this.name).price(this.price).traits(this.traits).amount(this.amount).build();
    }
    public CartDetail applyUpdateAmount(CartDetail.UpdateAmount u) {
        if(u.amount.value()==this.amount.value()) return this;
        return CartDetail.builder().variantId(this.variantId).imageKey(this.imageKey).name(this.name).price(this.price).traits(this.traits).amount(u.amount()).build();
    
    }
    public CartDetail applyUpdateInfo(CartDetail.UpdateInfo u) {
        if(!u.variantId.value().equals(this.variantId.value())) throw new IllegalArgumentException("Invalid argument");
        return CartDetail.builder().variantId(this.variantId).imageKey(u.imageKey).name(u.name).price(u.price).traits(u.traits()).amount(this.amount).build();
    }
    


}
