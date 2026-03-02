package vn.uit.edu.msshop.rating.domain.model;

import java.util.Objects;

import org.jspecify.annotations.NullMarked;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.uit.edu.msshop.rating.domain.model.valueobject.Content;
import vn.uit.edu.msshop.rating.domain.model.valueobject.Media;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingPoint;
import vn.uit.edu.msshop.rating.domain.model.valueobject.UserAvatar;
import vn.uit.edu.msshop.rating.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.Username;
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Rating {
    private RatingId id;
    private Content content;
    private Media media;
    private ProductId productId;
    private RatingPoint ratingPoint;
    private UserAvatar userAvatar;
    private UserId userId;
    private Username username;

    @Builder
    public static record Draft (
    RatingId id,
    Content content,
    Media media,
    ProductId productId,
    RatingPoint ratingPoint,
    UserAvatar userAvatar,
    UserId userId,
    Username username
    ) {

    }

    @Builder
    public static record Snapshot(
    RatingId id,
    Content content,
    Media media,
    ProductId productId,
    RatingPoint ratingPoint,
    UserAvatar userAvatar,
    UserId userId,
    Username username
    ) {

    }
    @Builder
    public static record UpdateInfo(
    RatingId id,
    Content content,
    RatingPoint ratingPoint
    ) {

    }
    @Builder
    public static record UpdateImage (Media media) {

    }
    @NullMarked
    public static Rating create(Draft d) {
        if(d==null) {
            throw new IllegalArgumentException("Draft must not be null");
        }
        if(d.id()==null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        return Rating.builder().id(d.id()).content(d.content()).media(d.media()).productId(d.productId()).ratingPoint(d.ratingPoint()).userAvatar(d.userAvatar()).userId(d.userId()).username(d.username()).build();
    }

    @NullMarked
    public static Rating reconstitue(Snapshot s) {
        if(s==null) {
            throw new IllegalArgumentException("Draft must not be null");
        }
        if(s.id()==null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        return Rating.builder().id(s.id()).content(s.content()).media(s.media()).productId(s.productId()).ratingPoint(s.ratingPoint()).userAvatar(s.userAvatar()).userId(s.userId()).username(s.username()).build();
    }
    @NullMarked
    public Rating applyUpdateInfo(UpdateInfo u) {
        if(u==null) {
            throw new IllegalArgumentException("Update info must not be null");
        }
        if(u.id()==null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if(isSameInfoWithUpdateInfo(u)) return this;
        return Rating.builder().id(this.id).content(u.content()).media(this.media).productId(this.productId).ratingPoint(u.ratingPoint()).userAvatar(this.userAvatar).username(this.username).userId(this.userId).build();
    }
    @NullMarked
    public Rating applyImageUpdate(UpdateImage u) {
        if(u==null) {
            throw new IllegalArgumentException("Update image can not be null");
        }
        return Rating.builder().id(this.id).content(this.content).media(u.media()).productId(this.productId).ratingPoint(this.ratingPoint).userAvatar(this.userAvatar).username(this.username).userId(this.userId).build();

    }
    @NullMarked
    public Snapshot snapShot() {
        return Snapshot.builder().id(this.id).content(this.content).media(this.media).productId(this.productId).ratingPoint(this.ratingPoint)
        .userAvatar(this.userAvatar).username(this.username).userId(this.userId).build();
    }
    @NullMarked
    private boolean isSameInfoWithUpdateInfo(UpdateInfo u) {
        return Objects.equals(u.content(), this.content)&&Objects.equals(u.ratingPoint(), this.ratingPoint);
    }
}
