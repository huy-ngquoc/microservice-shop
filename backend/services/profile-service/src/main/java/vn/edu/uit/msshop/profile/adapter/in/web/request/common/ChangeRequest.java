package vn.edu.uit.msshop.profile.adapter.in.web.request.common;

import java.util.function.Function;

import org.jspecify.annotations.NonNull;

import vn.edu.uit.msshop.profile.application.common.Change;

public record ChangeRequest<T>(
        T value) {
    public static <T, V> Change<V> toChange(
            ChangeRequest<T> c,

            @NonNull
            Function<T, V> map) {
        if ((c == null) || (c.value() == null)) {
            return Change.unchanged();
        }

        return Change.set(map.apply(c.value()));
    }
}
