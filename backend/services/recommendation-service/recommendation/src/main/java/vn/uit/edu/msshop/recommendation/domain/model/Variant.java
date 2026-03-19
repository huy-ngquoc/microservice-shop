package vn.uit.edu.msshop.recommendation.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantId;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantImages;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantName;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantTargets;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@Builder(
        access = AccessLevel.PRIVATE)
public class Variant {
    @EqualsAndHashCode.Include
    @NonNull
    private final VariantId id;

    private VariantName name;
    private VariantImages images;
    private VariantTargets targets;

    @Builder
    public static record Draft (
        VariantId id,
        VariantName name,
        VariantImages images,
        VariantTargets targets
    ) {
    }
    @Builder
    public static record Snapshot (
        VariantId id,
        VariantName name,
        VariantImages images,
        VariantTargets targets
    ) {

    }
    @Builder
    public static record UpdateInfo (
        VariantId id,
        VariantName name,
        VariantImages images,
        VariantTargets targets
    ) {

    }
    public static Variant create(Draft d) {
        if(d==null) throw new IllegalArgumentException("Draft must not be null");
        return Variant.builder().id(d.id).name(d.name).targets(d.targets).images(d.images).build();
    }

    public static Variant reconstitue(Snapshot s) {
        if(s==null) throw new IllegalArgumentException("Snapshot must not be null");
        return Variant.builder().id(s.id).name(s.name).targets(s.targets).images(s.images).build();
    }
    public  Variant applyUpdateInfo(UpdateInfo u) {
        if(u==null) throw new IllegalArgumentException("Update info must not be null");
        return Variant.builder().id(u.id).name(u.name).targets(u.targets).images(u.images).build();
    }
    public Snapshot snapshot() {
        return Variant.Snapshot.builder().id(this.id).name(this.name).targets(this.targets).images(this.images).build();
    }
}
