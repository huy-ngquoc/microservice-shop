package vn.uit.edu.msshop.account.adapter.in.web.request.common;
import java.util.function.Function;

import org.jspecify.annotations.NonNull;

import vn.edu.uit.msshop.account.application.common.Patch;



public record PatchRequest<T>(
        @NonNull
        Op op,

        T value) {
    public enum Op {
        KEEP,
        SET,
        CLEAR
    }

    public PatchRequest(
            final Op op,
            final T value) {
        if ((op == Op.SET) && (value == null)) {
            throw new IllegalArgumentException("PATCH op=SET requires value");
        }

        if (op == null) {
            this.op = Op.KEEP;
        } else {
            this.op = op;
        }

        this.value = value;
    }

    public static <T, V> Patch<V> toPatch(
            PatchRequest<T> p,
            Function<T, V> map) {
        if ((p == null) || (p.op() == Op.KEEP)) {
            return Patch.unchanged();
        }

        if (p.op() == Op.CLEAR) {
            return Patch.clear();
        }

        return Patch.set(map.apply(p.value()));
    }
}
