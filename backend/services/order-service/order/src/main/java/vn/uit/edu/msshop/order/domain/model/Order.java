package vn.uit.edu.msshop.order.domain.model;

import java.time.Instant;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.uit.edu.msshop.order.domain.model.valueobject.CreateAt;
import vn.uit.edu.msshop.order.domain.model.valueobject.Discount;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderDetail;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.OriginPrice;
import vn.uit.edu.msshop.order.domain.model.valueobject.ShippingFee;
import vn.uit.edu.msshop.order.domain.model.valueobject.ShippingInfo;
import vn.uit.edu.msshop.order.domain.model.valueobject.TotalPrice;
import vn.uit.edu.msshop.order.domain.model.valueobject.UpdateAt;
import vn.uit.edu.msshop.order.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.order.domain.model.valueobject.Version;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@Builder(
        access = AccessLevel.PRIVATE)
public final class Order {
    @EqualsAndHashCode.Include
    @NonNull
    private OrderId id;

    @NonNull
    private ShippingInfo shippingInfo;

    @NonNull
    private List<OrderDetail> details;

    @NonNull
    private OrderStatus status;

    @NonNull
    private UserId userId;

    @NonNull
    private OriginPrice originPrice;

    @NonNull
    private ShippingFee shippingFee;

    @NonNull
    private Discount discount;

    @NonNull
    private TotalPrice totalPrice;

    @NonNull
    private CreateAt createAt;

    @NonNull
    private UpdateAt updateAt;

    private Version version;

    @Builder
    public static record UpdateInfo(
        @NonNull
        OrderId  id,
        ShippingInfo shippingInfo,
        OrderStatus orderStatus
    ) {

    }

    @Builder 
    public static record Draft(
    @NonNull
    OrderId id,

    @NonNull
    ShippingInfo shippingInfo,

    @NonNull
    List<OrderDetail> details,

    @NonNull
    OrderStatus status,

    @NonNull
    UserId userId,

    @NonNull
    OriginPrice originPrice,

    @NonNull
    ShippingFee shippingFee,

    @NonNull
    Discount discount,

    @NonNull
    TotalPrice totalPrice,

    @NonNull
    CreateAt createAt,

    @NonNull
    UpdateAt updateAt
    ) {

    }

    @Builder
    public static record SnapShot(
         @NonNull
    OrderId id,

    @NonNull
    ShippingInfo shippingInfo,

    @NonNull
    List<OrderDetail> details,

    @NonNull
    OrderStatus status,

    @NonNull
    UserId userId,

    @NonNull
    OriginPrice originPrice,

    @NonNull
    ShippingFee shippingFee,

    @NonNull
    Discount discount,

    @NonNull
    TotalPrice totalPrice,

    @NonNull
    CreateAt createAt,

    @NonNull
    UpdateAt updateAt,
    @NonNull
    Version version
    ) {

    }
    @NullMarked
    public static Order create(final Draft d) {
        if (d == null) {
            throw new IllegalArgumentException("Draft must NOT be null");
        }

        if (d.id() == null) {
            throw new IllegalArgumentException("Id must NOT be null");
        }

        return Order.builder().id(d.id()).shippingInfo(d.shippingInfo()).details(d.details()).status(d.status()).userId(d.userId())
        .originPrice(d.originPrice()).shippingFee(d.shippingFee()).discount(d.discount()).totalPrice(d.totalPrice()).createAt(d.createAt()).updateAt(d.updateAt()).version(new Version(0)).build();
    }
    @NullMarked
    public static Order reconstitue(final SnapShot s) {
        if (s == null) {
            throw new IllegalArgumentException("Draft must NOT be null");
        }

        if (s.id() == null) {
            throw new IllegalArgumentException("Id must NOT be null");
        }

        return Order.builder().id(s.id()).shippingInfo(s.shippingInfo()).details(s.details()).status(s.status()).userId(s.userId())
        .originPrice(s.originPrice()).shippingFee(s.shippingFee()).discount(s.discount()).totalPrice(s.totalPrice()).createAt(s.createAt()).updateAt(s.updateAt()).version(s.version()).build();
    
    }
    @NullMarked
    public Order applyUpdateInfo(final UpdateInfo u) {
        if (u == null) {
            throw new IllegalArgumentException("Update must NOT be null");
        }

        if (this.isSameInfoWithUpdateInfo(u)) {
            return this;
        }

        OrderStatus newStatus = u.orderStatus()!=null?u.orderStatus():this.status;
        ShippingInfo newShippingInfo = u.shippingInfo()!=null?u.shippingInfo():this.shippingInfo;
        Instant updateTime = (u.orderStatus()==null&&u.shippingInfo()==null)?this.updateAt.value():Instant.now();
        return Order.builder().id(this.id).shippingInfo(newShippingInfo).details(this.details).status(newStatus).userId(this.userId)
        .originPrice(this.originPrice).shippingFee(this.shippingFee).discount(this.discount).totalPrice(this.totalPrice).createAt(this.createAt).updateAt(new UpdateAt(updateTime)).version(this.version).build();
    }

    @NullMarked
    public SnapShot snapShot() {
        return SnapShot.builder().id(this.id).shippingInfo(this.shippingInfo).details(this.details).status(this.status).userId(this.userId)
        .originPrice(this.originPrice).shippingFee(this.shippingFee).discount(this.discount).totalPrice(this.totalPrice).createAt(this.createAt).updateAt(this.updateAt).version(this.version).build();
    }
    @NullMarked
    @SuppressWarnings("java:S1067")
    private boolean isSameInfoWithUpdateInfo(
            final UpdateInfo u) {
        return u.orderStatus().value().equals(this.status.value())&&isShippingInfoSame(u.shippingInfo());
    }
    private boolean isShippingInfoSame(final ShippingInfo s) {
        return (s.address().equals(this.shippingInfo.address()))&&(s.email().equals(this.shippingInfo.email()))
        &&(s.fullName().equals(this.shippingInfo.fullName()))&&(s.phone().equals(this.shippingInfo.phone()));
    }
}
