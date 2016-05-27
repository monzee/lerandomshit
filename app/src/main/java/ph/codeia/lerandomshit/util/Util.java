package ph.codeia.lerandomshit.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class Util {
    private Util() {
        // NOPE.
    }

    public static <T> T tap(T t, Consumer<T> init) {
        init.accept(t);
        return t;
    }

    @NonNull
    public static <T> T either(@Nullable T maybeNull, @NonNull T defaultValue) {
        return maybeNull == null ? defaultValue : maybeNull;
    }
}
