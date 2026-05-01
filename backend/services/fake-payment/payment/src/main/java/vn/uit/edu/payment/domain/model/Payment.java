package vn.uit.edu.payment.domain.model;

import java.time.Instant;
import java.util.Objects;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.uit.edu.payment.domain.model.valueobject.CreateAt;
import vn.uit.edu.payment.domain.model.valueobject.Currency;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentMethod;
import vn.uit.edu.payment.domain.model.valueobject.PaymentStatus;
import vn.uit.edu.payment.domain.model.valueobject.PaymentValue;
import vn.uit.edu.payment.domain.model.valueobject.UpdateAt;
import vn.uit.edu.payment.domain.model.valueobject.UserEmail;
import vn.uit.edu.payment.domain.model.valueobject.UserId;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@Builder(
        access = AccessLevel.PRIVATE)
public class Payment {
    @EqualsAndHashCode.Include
    @NonNull
    private final PaymentId paymentId;
    
    private final CreateAt createAt;

    private final Currency currency;

    private final OrderId orderId;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private final PaymentValue paymentValue;

    private UpdateAt updateAt;

    private final UserId userId;

    private final UserEmail userEmail;



    @Builder
    public static record Draft (
    @NonNull
    PaymentId paymentId,
    CreateAt createAt,
    Currency currency,
    OrderId orderId,
    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus,
    PaymentValue paymentValue,
    UpdateAt updateAt,
    UserId userId,
    UserEmail userEmail
    ) {
        public Draft {
            if(paymentId==null) {
                 throw new IllegalArgumentException("Id must NOT be null");
            }
            if(createAt==null){
                throw new IllegalArgumentException("Create time must not be null");
            } 
            if(currency==null) {
                throw new IllegalArgumentException("Currency must not be null");
            } 
            if(orderId==null) {
                throw new IllegalArgumentException("Order id must not be null");
            }
            if(paymentMethod==null) {
                throw new IllegalArgumentException("Payment method must not be null");
            }
            if(paymentStatus==null) {
                throw new IllegalArgumentException("Payment status must not be null");
            }
            if(paymentValue == null) {
                throw new IllegalArgumentException("Payment value must not be null");
            }
            if(userId==null) {
                throw new IllegalArgumentException("User id must not be null");
            }
            if(userEmail==null) {
                throw new IllegalArgumentException("Invalid user email");
            }
        }

    }

    @Builder
    public static record Snapshot (
        @NonNull
        PaymentId paymentId,
        CreateAt createAt,
        Currency currency,
        OrderId orderId,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        PaymentValue paymentValue,
        UpdateAt updateAt,
        UserId userId,
        UserEmail userEmail
    ) {
        public Snapshot {
            if(paymentId==null) {
                 throw new IllegalArgumentException("Id must NOT be null");
            }
            if(createAt==null){
                throw new IllegalArgumentException("Create time must not be null");
            } 
            if(currency==null) {
                throw new IllegalArgumentException("Currency must not be null");
            } 
            if(orderId==null) {
                throw new IllegalArgumentException("Order id must not be null");
            }
            if(paymentMethod==null) {
                throw new IllegalArgumentException("Payment method must not be null");
            }
            
            if(paymentValue == null) {
                throw new IllegalArgumentException("Payment value must not be null");
            }
            if(userId==null) {
                throw new IllegalArgumentException("User id must not be null");
            }
            if(userEmail==null) {
                throw new IllegalArgumentException("Invalid user email");
            }
        }
    }

    @Builder
    public static record UpdateInfo(
        @NonNull
        PaymentId paymentId,
        Currency currency,
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod

    ) {
    

    }
    @NullMarked
    public static Payment create(Draft d) {
        if (d == null) {
            throw new IllegalArgumentException("Draft must NOT be null");
        }
        return Payment.builder().paymentId(d.paymentId()).createAt(d.createAt()).currency(d.currency()).orderId(d.orderId()).paymentMethod(d.paymentMethod())
        .paymentStatus(d.paymentStatus()).paymentValue(d.paymentValue()).updateAt(d.updateAt()).userId(d.userId()).userEmail(d.userEmail).build();
    }
    @NullMarked
    public static Payment reconstitue(Snapshot s) {
        if(s==null) {
            throw new IllegalArgumentException("Snapshot must not be null");

        }
        return Payment.builder().paymentId(s.paymentId()).createAt(s.createAt()).currency(s.currency()).orderId(s.orderId()).paymentMethod(s.paymentMethod())
        .paymentStatus(s.paymentStatus()).paymentValue(s.paymentValue()).updateAt(s.updateAt()).userId(s.userId()).userEmail(s.userEmail).build();
    }
    @NullMarked
    public Snapshot snapshot() {
        return Snapshot.builder().paymentId(this.paymentId).createAt(this.createAt).currency(this.currency).orderId(this.orderId).paymentMethod(this.paymentMethod)
        .paymentStatus(this.paymentStatus).paymentValue(this.paymentValue).updateAt(this.updateAt).userId(this.userId).userEmail(this.userEmail).build();
    
    }
    @NullMarked
    public Payment applyUpdateInfo(final UpdateInfo u) {
        if (u == null) {
            throw new IllegalArgumentException("Update must NOT be null");
        }
        if(isSameInfoWithUpdateInfo(u)) {
            return this;
        } 
        return Payment.builder().paymentId(paymentId).createAt(createAt).currency(u.currency()).orderId(orderId).paymentMethod(u.paymentMethod())
        .paymentStatus(u.paymentStatus()).paymentValue(paymentValue).updateAt(new UpdateAt(Instant.now())).userId(this.userId).userEmail(this.userEmail).build();

    }
    @NullMarked
    @SuppressWarnings("java:S1067")
    private boolean isSameInfoWithUpdateInfo(
            final UpdateInfo u) {
        return Objects.equals(u.currency(), this.currency)
                && Objects.equals(u.paymentStatus(), this.paymentStatus)
                && Objects.equals(u.paymentMethod(), this.paymentMethod);
                
    }
    public void setPaymentStatus(PaymentStatus s) {
        this.paymentStatus=s;
    }
}
