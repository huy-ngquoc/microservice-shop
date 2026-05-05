package vn.edu.uit.msshop.shared.adapter.in.web.request;

import java.util.function.Function;

import org.jspecify.annotations.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import vn.edu.uit.msshop.shared.application.dto.Change;

public record ChangeRequest<T>(
        @JsonValue
        T value) {
    @JsonCreator(
            mode = JsonCreator.Mode.DELEGATING)
    public ChangeRequest {
    }

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
