package vn.uit.edu.msshop.order.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.uit.edu.msshop.order.domain.model.valueobject.SoldCount;
import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;
import vn.uit.edu.msshop.order.domain.model.valueobject.Version;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@Builder(
        access = AccessLevel.PRIVATE)
public class VariantSoldCount {
    @EqualsAndHashCode.Include
    private VariantId id;
    private SoldCount soldCount;
    private Version version;
    @Builder
    public static record UpdateInfo(VariantId id, SoldCount soldCount) {

    }
    @Builder
    public static record Draft (VariantId id, SoldCount soldCount) {

    }
    @Builder
    public static record Snapshot(VariantId id,SoldCount soldCount, Version version) {

    }
    
    public static VariantSoldCount create(Draft draft) {
        if (draft == null) {
            throw new IllegalArgumentException("Draft must NOT be null");
        }

        if (draft.id() == null) {
            throw new IllegalArgumentException("Id must NOT be null");
        }
        return VariantSoldCount.builder().id(draft.id()).soldCount(draft.soldCount()).version(new Version(null)).build();
    }
    public static VariantSoldCount reconstitue(Snapshot snapshot) {
        if(snapshot==null) {
            throw new IllegalArgumentException("Snapshot must not be null");
        }
        if(snapshot.id()==null) {
            throw new IllegalArgumentException("Id mus NOT be null");
        }
        return VariantSoldCount.builder().id(snapshot.id()).soldCount(snapshot.soldCount()).version(snapshot.version()).build();
    }
    public VariantSoldCount applyUpdateInfo(UpdateInfo u) {
        if(u==null) throw new IllegalArgumentException("Update info must not be null");
        if(u.id()==null) throw new IllegalArgumentException("Invalid update info");
        return VariantSoldCount.builder().id(this.id).soldCount(u.soldCount()).version(this.version).build();
    }
}
