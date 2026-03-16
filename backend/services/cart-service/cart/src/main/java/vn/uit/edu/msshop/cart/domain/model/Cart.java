package vn.uit.edu.msshop.cart.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@Builder(
        access = AccessLevel.PRIVATE)
public class Cart {
    private UserId userId;
    private List<CartDetail> details;
    @Builder
    public static record Draft(UserId userId, List<CartDetail.Draft> detailDrafts) {

    }
    @Builder
    public static record Snapshot(UserId userId, List<CartDetail.Snapshot> detailSnapshots) {

    } 
    @Builder
    public static record UpdateAmount(UserId userId, List<CartDetail.UpdateAmount> detailUpdateAmounts) {

    } 
    @Builder
    public static record UpdateInfo(UserId userId, List<CartDetail.UpdateInfo> detailUpdateInfos) {

    } 
    public static Cart create(Cart.Draft d) {
        if(d==null) throw new IllegalArgumentException("Invalid draft");
        return Cart.builder().userId(d.userId()).details(d.detailDrafts.stream().map(item->CartDetail.create(item)).toList()).build();
    }
    public Cart.Snapshot snapshot() {
        return Cart.Snapshot.builder().userId(this.userId).detailSnapshots(this.details.stream().map(item->item.snapshot()).toList()).build();
    }
    public Cart applyUpdateAmount(Cart.UpdateAmount u) {
        if (u == null) throw new IllegalArgumentException("Invalid update info");
    if (!u.userId.value().equals(this.userId.value())) 
        throw new IllegalArgumentException("User id does not match");

    
    List<CartDetail> newDetails = this.details.stream()
        .map(currentDetail -> {
            
            return u.detailUpdateAmounts().stream()
                .filter(update -> update.variantId().equals(currentDetail.getVariantId()))
                .findFirst()
                
                .map(currentDetail::applyUpdateAmount) 
                
                .orElse(currentDetail);
        })
        .collect(Collectors.toCollection(ArrayList::new)); 

    
    return Cart.builder()
               .userId(this.userId)
               .details(newDetails)
               .build();
    }
    public Cart applyUpdateInfo(Cart.UpdateInfo u) {
        if(u==null) throw new IllegalArgumentException("Invalid update info");
        if(!u.userId.value().equals(this.userId.value())) throw new IllegalArgumentException("Invalid update info, user id does not match");
        for(CartDetail.UpdateInfo cUpdateInfo: u.detailUpdateInfos()) {
            final var detail = findByVariantId(cUpdateInfo.variantId());
            if(detail!=null) {
                detail.applyUpdateInfo(cUpdateInfo);
            }
        }
        return Cart.builder().userId(this.userId).details(this.details).build();
    }
    public static Cart createEmpty(UserId userId) {
        return Cart.builder().userId(userId).details(new ArrayList<>()).build();
    }
    public CartDetail findByVariantId(VariantId id) {
        for(CartDetail c: details) {
            if(c.getVariantId().value().equals(id.value())) return c;
        }
        return null;
    }
    public void removeByVariantId(VariantId id) {
        CartDetail toDelete = findByVariantId(id);
        if (!(this.details instanceof ArrayList)) {
        this.details = new ArrayList<>(this.details);
    }
        if(toDelete!=null) details.remove(toDelete);
    }
    public Cart addItems(List<CartDetail> details) {
        if (!(this.details instanceof ArrayList)) {
        this.details = new ArrayList<>(this.details);
    }
        for(CartDetail d: details) {
            if(findByVariantId(d.getVariantId())==null) {
                this.details.add(d);
            }
        }
        return this;
    }
}
