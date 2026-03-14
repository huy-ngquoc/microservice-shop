package vn.edu.uit.msshop.product.shared.adapter.in.web.request;

import java.util.function.Function;

import org.jspecify.annotations.NonNull;

import vn.edu.uit.msshop.product.shared.application.dto.Change;

public record ChangeRequest<T>(
        T value) {
    public static <T, V> Change<V> toChange(
            ChangeRequest<T> c,

            @NonNull
            Function<@NonNull T, @NonNull V> map) {
        if ((c == null) || (c.value() == null)) {
            return Change.unchanged();
        }

        return Change.set(map.apply(c.value()));
    }
}
