package vn.uit.edu.msshop.rating.domain.model;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.uit.edu.msshop.rating.domain.model.valueobject.CreateAt;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingCount;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingPoint;
import vn.uit.edu.msshop.rating.domain.model.valueobject.UpdateAt;


@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class RatingInfo {
    private ProductId productId;
    private RatingCount ratingCount;
    private RatingPoint totalPoint;
    private CreateAt createAt;
    private UpdateAt updateAt;
    
    @Builder
    public static record Draft(ProductId productId, RatingCount ratingCount, RatingPoint totalPoint, CreateAt createAt, UpdateAt updateAt) {

    }
    @Builder
    public static record Snapshot(ProductId productId, RatingCount ratingCount, RatingPoint totalPoint, CreateAt createAt, UpdateAt updateAt) {

    }
    @Builder
    public static record UpdateInfo(ProductId productId, RatingCount ratingCount, RatingPoint totalPoint) {

    }

    public static RatingInfo create(Draft d) {
        if(d.productId==null)  throw new IllegalArgumentException("Invalid id");
        return RatingInfo.builder().productId(d.productId()).ratingCount(d.ratingCount()).totalPoint(d.totalPoint())
        .createAt(d.createAt())
        .updateAt(d.updateAt())
        .build();
    }
    public static RatingInfo reconstitue(Snapshot s) {
        if(s.productId==null)  throw new IllegalArgumentException("Invalid id");
        return RatingInfo.builder().productId(s.productId()).ratingCount(s.ratingCount()).totalPoint(s.totalPoint())
        .createAt(s.createAt())
        .updateAt(s.updateAt())
        .build();
    }
    public RatingInfo applyUpdateInfo(UpdateInfo updateInfo) {
        if(updateInfo.productId==null) {
            throw new IllegalArgumentException("Invalid id");
        }
        if(!updateInfo.productId.value().equals(this.productId.value())) {
            throw new IllegalArgumentException("Invalid update info");
        }
        return RatingInfo.builder().productId(this.productId).ratingCount(updateInfo.ratingCount).totalPoint(updateInfo.totalPoint())
        .createAt(this.createAt)
        .updateAt(new UpdateAt(Instant.now()))
        .build();
    }
    public Snapshot snapshot() {
        return RatingInfo.Snapshot.builder().productId(this.productId).ratingCount(this.ratingCount).totalPoint(this.totalPoint).build();
    }
    public RatingInfo increaseRatingPoint(RatingPoint ratingPoint) {
        
        float nextPoint = (this.totalPoint.value()+ratingPoint.value())/(this.ratingCount.value()+1);
        return RatingInfo.builder().productId(this.productId).ratingCount(new RatingCount(this.ratingCount.value()+1)).totalPoint(new RatingPoint(nextPoint))
        .createAt(this.createAt)
        .updateAt(new UpdateAt(Instant.now()))
        .build();
    }
    public RatingInfo decreaseRatingPoint(RatingPoint ratingPoint) {
        if(this.ratingCount.value()<=1) {
            return RatingInfo.builder().productId(this.productId).ratingCount(new RatingCount(0)).totalPoint(new RatingPoint(0)).build();
        }
        float prevPoint = (this.totalPoint.value()-ratingPoint.value())/(this.ratingCount.value()-1);
        return RatingInfo.builder().productId(this.productId).ratingCount(new RatingCount(this.ratingCount.value()+1)).totalPoint(new RatingPoint(prevPoint))
        .createAt(this.createAt)
        .updateAt(new UpdateAt(Instant.now()))
        .build();
    }
 }
